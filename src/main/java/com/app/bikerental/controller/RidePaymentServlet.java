package com.app.bikerental.controller;

import com.app.bikerental.dao.BikeDAO;
import com.app.bikerental.model.Bike;
import com.app.bikerental.model.Ride;
import com.app.bikerental.model.Payment;
import com.app.bikerental.dao.RideDAO;
import com.app.bikerental.util.PaymentUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/processRidePayment")
public class RidePaymentServlet extends HttpServlet {
    // In RidePaymentServlet.java
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Get payment details from form
            String name = request.getParameter("name");
            String cardNumber = request.getParameter("cardNumber");
            double amount = Double.parseDouble(request.getParameter("amount"));
            String rideId = request.getParameter("rideId");

            // Validate inputs
            if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
                request.setAttribute("error", "Card number must be exactly 16 digits.");
                request.getRequestDispatcher("paymentForm.jsp").forward(request, response);
                return;
            }

            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than 0.");
                request.getRequestDispatcher("paymentForm.jsp").forward(request, response);
                return;
            }

            // Get the ride from session
            Ride ride = (Ride) session.getAttribute("rideForPayment");
            if (ride == null) {
                response.sendRedirect("ride?action=dashboard&error=Ride+not+found");
                return;
            }

            // Create payment record
            Payment payment = new Payment(
                    name,
                    List.of("Bike Ride - " + ride.getRideId()),
                    amount,
                    cardNumber,
                    "Completed"
            );

            // Save payment
            PaymentUtil.savePayment(payment);

            // Update ride status to completed
            ride.setStatus(Ride.STATUS_COMPLETED);
            RideDAO.updateRide(ride);

            // Update bike availability
            Bike bike = BikeDAO.getBikeById(ride.getBikeId());
            if (bike != null) {
                bike.setAvailable(true);
                bike.setAvailability(Bike.STATUS_AVAILABLE);
                BikeDAO.updateBike(bike);
            }

            // Set attributes for success page
            request.setAttribute("name", name);
            request.setAttribute("amount", amount);
            session.setAttribute("latestPayment", payment);

            // Clean up session
            session.removeAttribute("rideForPayment");

            // Forward to success page
            request.getRequestDispatcher("paymentSuccess.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format.");
            request.getRequestDispatcher("paymentForm.jsp").forward(request, response);
        }
    }
}