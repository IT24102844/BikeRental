package com.app.bikerental.controller;

import com.app.bikerental.dao.BikeDAO;
import com.app.bikerental.dao.RideDAO;
import com.app.bikerental.model.Bike;
import com.app.bikerental.model.Ride;
import com.app.bikerental.util.RideUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/ride")
public class RideController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Default to dashboard if no action specified
        if (action == null) {
            resp.sendRedirect("ride?action=dashboard");
            return;
        }

        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            switch (action) {
                case "book":
                    showBookingPage(req, resp);
                    break;
                case "history":
                    showRideHistory(req, resp, userId);
                    break;
                case "findShared":
                    findSharedRides(req, resp);
                    break;
                case "dashboard":
                    showDashboard(req, resp, userId);
                    break;
                default:
                    resp.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("ride_dashboard.jsp?error=An+error+occurred");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        switch (action) {
            case "book":
                bookRide(req, resp, userId);
                break;
            case "updateStatus":
                updateRideStatus(req, resp);
                break;
            case "joinShared":
                joinSharedRide(req, resp, userId);
                break;
            default:
                resp.sendRedirect("index.jsp");
        }
    }

    private void showBookingPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Bike> availableBikes = BikeDAO.getAllBikes().stream()
                .filter(b -> b.isAvailable() && b.getAvailability().equals(Bike.STATUS_AVAILABLE))
                .collect(Collectors.toList());
        req.setAttribute("availableBikes", availableBikes);
        req.getRequestDispatcher("ride_booking.jsp").forward(req, resp);
    }

    private void showRideHistory(HttpServletRequest req, HttpServletResponse resp, String userId)
            throws ServletException, IOException {
        List<Ride> rides = RideDAO.getRidesByRider(userId);
        req.setAttribute("rides", rides);
        req.getRequestDispatcher("ride_history.jsp").forward(req, resp);
    }

    private void findSharedRides(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pickup = req.getParameter("pickup");
        String drop = req.getParameter("drop");

        // Validate required parameters
        if (pickup == null || pickup.trim().isEmpty() ||
                drop == null || drop.trim().isEmpty()) {
            resp.sendRedirect("shared_rides.jsp?error=Pickup+and+drop+locations+are+required");
            return;
        }

        try {
            List<Ride> sharedRides = RideUtil.matchSharedRides(
                    RideDAO.getActiveSharedRides(),
                    pickup.trim(),
                    drop.trim()
            );

            req.setAttribute("sharedRides", sharedRides);
            req.setAttribute("searchPickup", pickup);
            req.setAttribute("searchDrop", drop);
            req.getRequestDispatcher("shared_rides.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("shared_rides.jsp?error=Error+searching+shared+rides");
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp, String userId)
            throws ServletException, IOException {
        List<Ride> activeRides = RideDAO.getRidesByRider(userId).stream()
                .filter(r -> !r.getStatus().equals(Ride.STATUS_COMPLETED))
                .filter(r -> !r.getStatus().equals(Ride.STATUS_CANCELLED))
                .collect(Collectors.toList());

        req.setAttribute("activeRides", activeRides);
        req.getRequestDispatcher("ride_dashboard.jsp").forward(req, resp);
    }

    private void bookRide(HttpServletRequest req, HttpServletResponse resp, String userId)
            throws IOException {
        String bikeId = req.getParameter("bikeId");
        if (bikeId == null || bikeId.isEmpty()) {
            resp.sendRedirect("ride_booking.jsp?error=No+bike+selected");
            return;
        }

        Bike bike = BikeDAO.getBikeById(bikeId);
        if (bike == null || !bike.isAvailable()) {
            resp.sendRedirect("ride_booking.jsp?error=Bike+not+available");
            return;
        }

        Ride ride = new Ride();
        ride.setRideId(RideDAO.generateRideId());
        ride.setRiderId(userId);
        ride.setBikeId(bikeId);
        ride.setPickupLocation(req.getParameter("pickupLocation"));
        ride.setDropLocation(req.getParameter("dropLocation"));
        ride.setStatus(Ride.STATUS_BOOKED);
        ride.setTimestamp(LocalDateTime.now().toString());
        ride.setRideType(req.getParameter("rideType"));
        ride.setAvailableSeats(ride.getRideType().equals(Ride.TYPE_SHARED) ? 1 : 0);

        bike.setAvailable(false);
        bike.setAvailability(Bike.STATUS_UNAVAILABLE);

        try {
            RideDAO.addRide(ride);
            BikeDAO.updateBike(bike);
            resp.sendRedirect("ride?action=dashboard&msg=Ride+booked+successfully");
        } catch (Exception e) {
            resp.sendRedirect("ride_booking.jsp?error=Booking+failed");
        }
    }

    private void updateRideStatus(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String rideId = req.getParameter("rideId");
        String newStatus = req.getParameter("status");

        Optional<Ride> optionalRide = RideDAO.getRideById(rideId);
        if (optionalRide.isPresent()) {
            Ride ride = optionalRide.get();
            String previousStatus = ride.getStatus();
            ride.setStatus(newStatus);

            // Update bike availability if changing to/from certain statuses
            if (newStatus.equals(Ride.STATUS_CANCELLED) ||
                    newStatus.equals(Ride.STATUS_COMPLETED)) {

                Bike bike = BikeDAO.getBikeById(ride.getBikeId());
                if (bike != null) {
                    bike.setAvailable(true);
                    bike.setAvailability(Bike.STATUS_AVAILABLE);
                    BikeDAO.updateBike(bike);
                }
            }
            else if (previousStatus.equals(Ride.STATUS_BOOKED) &&
                    newStatus.equals(Ride.STATUS_IN_PROGRESS)) {
                // Bike remains unavailable during ride
                Bike bike = BikeDAO.getBikeById(ride.getBikeId());
                if (bike != null) {
                    bike.setAvailable(false);
                    bike.setAvailability(Bike.STATUS_UNAVAILABLE);
                    BikeDAO.updateBike(bike);
                }
            }

            RideDAO.updateRide(ride);
            resp.sendRedirect("ride?action=dashboard&msg=Status+updated");
        } else {
            resp.sendRedirect("ride?action=dashboard&error=Ride+not+found");
        }
    }

    private void joinSharedRide(HttpServletRequest req, HttpServletResponse resp, String userId)
            throws IOException {
        String rideId = req.getParameter("rideId");

        Optional<Ride> optionalRide = RideDAO.getRideById(rideId);
        if (optionalRide.isPresent()) {
            Ride ride = optionalRide.get();
            if (ride.getAvailableSeats() > 0) {
                ride.setAvailableSeats(ride.getAvailableSeats() - 1);
                RideDAO.updateRide(ride);
                resp.sendRedirect("ride?action=dashboard&msg=Joined+shared+ride");
            } else {
                resp.sendRedirect("shared_rides.jsp?error=No+seats+available");
            }
        } else {
            resp.sendRedirect("shared_rides.jsp?error=Ride+not+found");
        }
    }
}