package com.app.bikerental.controller;

import com.app.bikerental.dao.BikeDAO;
import com.app.bikerental.dao.RideDAO;
import com.app.bikerental.model.Bike;
import com.app.bikerental.model.BikeRequest;
import com.app.bikerental.model.Ride;
import com.app.bikerental.util.BikeRequestQueue;
import com.app.bikerental.util.IDGenerator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/ride")
public class RideController extends HttpServlet {
    private static final BikeRequestQueue requestQueue = new BikeRequestQueue();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

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
                case "dashboard":
                    showDashboard(req, resp, userId);
                    break;
                case "viewQueue":
                    viewQueue(req, resp);
                    break;
                case "processNext":
                    processNextRequest(req, resp);
                    break;
                case "clearQueue":
                    clearQueue(req, resp);
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
            case "processRequest":
                processSpecificRequest(req, resp);
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
        String bikeType = req.getParameter("bikeType"); // Added bikeType parameter
        String pickupLocation = req.getParameter("pickupLocation");
        String dropLocation = req.getParameter("dropLocation");

        if (bikeId == null || bikeId.isEmpty()) {
            resp.sendRedirect("ride_booking.jsp?error=No+bike+selected");
            return;
        }

        // Create a new bike request with bike type information
        String requestId = IDGenerator.generateId("REQ");
        BikeRequest request = new BikeRequest(requestId, userId, bikeId, bikeType,
                pickupLocation, dropLocation);
        requestQueue.enqueue(request);

        // Process the request
        processRequest(request);

        resp.sendRedirect("ride?action=dashboard&msg=Ride+request+submitted");
    }

    private void processRequest(BikeRequest request) {
        try {
            request.setStatus("PROCESSING");

            Bike bike = BikeDAO.getBikeById(request.getBikeId());
            if (bike == null || !bike.isAvailable()) {
                request.setStatus("CANCELLED");
                return;
            }

            // Create the ride using proper constructor
            Ride ride = new Ride(
                    RideDAO.generateRideId(),
                    request.getUserId(),
                    bike.getDriverId(),  // Make sure this is properly set
                    request.getBikeId(),
                    request.getPickupLocation(),
                    request.getDropLocation(),
                    Ride.STATUS_BOOKED,
                    LocalDateTime.now().toString()
            );

            // Update bike availability
            bike.setAvailable(false);
            bike.setAvailability(Bike.STATUS_UNAVAILABLE);

            // Save to database
            RideDAO.addRide(ride);
            BikeDAO.updateBike(bike);

            request.setStatus("COMPLETED");
        } catch (Exception e) {
            e.printStackTrace();
            request.setStatus("CANCELLED");
        }
    }
    // New method to process electric bike requests differently if needed
    private void processElectricBikeRequest(BikeRequest request) {
        // Special processing for electric bikes
        processRequest(request); // Default processing for now
    }

    // New method to process manual bike requests differently if needed
    private void processManualBikeRequest(BikeRequest request) {
        // Special processing for manual bikes
        processRequest(request); // Default processing for now
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

            if (newStatus.equals(Ride.STATUS_COMPLETED)) {
                // Store ride in session for payment
                HttpSession session = req.getSession();
                session.setAttribute("rideForPayment", ride);
                Bike bike = BikeDAO.getBikeById(ride.getBikeId());
                if (bike != null) {
                    bike.setAvailable(true);
                    bike.setAvailability(Bike.STATUS_AVAILABLE);
                    BikeDAO.updateBike(bike);
                }
                resp.sendRedirect("paymentForm.jsp");
                return;
            }

            if (newStatus.equals(Ride.STATUS_CANCELLED)) {
                Bike bike = BikeDAO.getBikeById(ride.getBikeId());
                if (bike != null) {
                    bike.setAvailable(true);
                    bike.setAvailability(Bike.STATUS_AVAILABLE);
                    BikeDAO.updateBike(bike);
                }
            }
            else if (previousStatus.equals(Ride.STATUS_BOOKED) &&
                    newStatus.equals(Ride.STATUS_IN_PROGRESS)) {
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

    private void viewQueue(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<BikeRequest> requests = new ArrayList<>();
        BikeRequestQueue tempQueue = new BikeRequestQueue();

        // Copy the queue to view its contents
        synchronized (requestQueue) {
            while (!requestQueue.isEmpty()) {
                BikeRequest request = requestQueue.dequeue();
                requests.add(request);
                tempQueue.enqueue(request);
            }

            // Restore the queue
            while (!tempQueue.isEmpty()) {
                requestQueue.enqueue(tempQueue.dequeue());
            }
        }

        req.setAttribute("requests", requests);
        req.getRequestDispatcher("request_queue.jsp").forward(req, resp);
    }

    private void processNextRequest(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            BikeRequest request = requestQueue.dequeue();
            if (request != null) {
                processRequest(request);
                resp.sendRedirect("ride?action=viewQueue&msg=Request+processed+successfully");
            } else {
                resp.sendRedirect("ride?action=viewQueue&error=No+requests+in+queue");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("ride?action=viewQueue&error=Error+processing+request");
        }
    }

    private void clearQueue(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        requestQueue.clear();
        resp.sendRedirect("ride?action=viewQueue&msg=Queue+cleared+successfully");
    }

    private void processSpecificRequest(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String requestId = req.getParameter("requestId");
        try {
            BikeRequestQueue tempQueue = new BikeRequestQueue();
            BikeRequest targetRequest = null;

            synchronized (requestQueue) {
                while (!requestQueue.isEmpty()) {
                    BikeRequest request = requestQueue.dequeue();
                    if (request.getRequestId().equals(requestId)) {
                        targetRequest = request;
                    } else {
                        tempQueue.enqueue(request);
                    }
                }

                // Restore the queue
                while (!tempQueue.isEmpty()) {
                    requestQueue.enqueue(tempQueue.dequeue());
                }
            }

            if (targetRequest != null) {
                processRequest(targetRequest);
                resp.sendRedirect("ride?action=viewQueue&msg=Request+processed+successfully");
            } else {
                resp.sendRedirect("ride?action=viewQueue&error=Request+not+found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("ride?action=viewQueue&error=Error+processing+request");
        }
    }
}