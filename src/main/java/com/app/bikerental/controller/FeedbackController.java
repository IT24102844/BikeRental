package com.app.bikerental.controller;

import com.app.bikerental.dao.FeedbackDAO;
import com.app.bikerental.dao.RideDAO;
import com.app.bikerental.model.Feedback;
import com.app.bikerental.model.Ride;
import com.app.bikerental.model.User;
import com.app.bikerental.model.Admin;
import com.app.bikerental.model.Driver;
import com.app.bikerental.util.IDGenerator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/feedback")
public class FeedbackController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        if ("create".equals(action) && !(user instanceof Admin)) {
            // Show feedback creation form for riders
            String rideId = req.getParameter("rideId");
            Optional<Ride> ride = RideDAO.getRideById(rideId);

            if (ride.isPresent()) {
                req.setAttribute("ride", ride.get());
                req.getRequestDispatcher("createFeedback.jsp").forward(req, resp);
            } else {
                resp.sendRedirect("ride?action=dashboard?error=Ride+not+found");
            }
        }
        else if ("manage".equals(action) && user instanceof Admin) {
            // Admin feedback management
            List<Feedback> feedbacks = FeedbackDAO.getAllFeedbacks();
            req.setAttribute("feedbacks", feedbacks);
            req.getRequestDispatcher("manageFeedbacks.jsp").forward(req, resp);
        }
        else {
            // Default feedback view based on role
            List<Feedback> feedbacks;
            if (user instanceof Driver) {
                feedbacks = FeedbackDAO.getFeedbacksByDriver(user.getId());
            } else {
                feedbacks = FeedbackDAO.getFeedbacksByRider(user.getId());
            }
            req.setAttribute("feedbacks", feedbacks);
            req.getRequestDispatcher("viewFeedbacks.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        if ("submit".equals(action)) {
            submitFeedback(req, resp, user);
        }
        else if ("delete".equals(action) && user instanceof Admin) {
            deleteFeedback(req, resp);
        }
        else {
            resp.sendRedirect("ride?action=dashboard?error=Invalid+action");
        }
    }

    private void submitFeedback(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        String rideId = req.getParameter("rideId");
        String driverId = req.getParameter("driverId");
        int rating = Integer.parseInt(req.getParameter("rating"));
        String comment = req.getParameter("comment");

        // Validate the rider is associated with this ride
        Optional<Ride> ride = RideDAO.getRideById(rideId);
        if (!ride.isPresent() || !ride.get().getRiderId().equals(user.getId())) {
            resp.sendRedirect("ride?action=dashboard&error=Invalid+ride");
            return;
        }

        // Check if feedback already exists
        if (FeedbackDAO.getFeedbackByRide(rideId).isPresent()) {
            resp.sendRedirect("ride?action=dashboard&error=Feedback+already+submitted");
            return;
        }

        Feedback feedback = new Feedback(
                IDGenerator.generateId("FB"),
                rideId,
                user.getId(),
                driverId,
                rating,
                comment
        );

        if (FeedbackDAO.addFeedback(feedback)) {
            // Success - redirect to ride dashboard with success message
            resp.sendRedirect("ride?action=dashboard&msg=Feedback+submitted+successfully");
        } else {
            // Failure - redirect back with error
            resp.sendRedirect("ride?action=dashboard&error=Failed+to+submit+feedback");
        }
    }

    private void deleteFeedback(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String feedbackId = req.getParameter("id");
        HttpSession session = req.getSession(false); // don't create new session if none exists

        if (session == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        if (feedbackId == null || feedbackId.isEmpty()) {
            resp.sendRedirect("feedback?action=manage&error=Invalid+feedback+ID");
            return;
        }

        Feedback feedback = FeedbackDAO.getFeedbackById(feedbackId);
        if (feedback == null) {
            resp.sendRedirect("feedback?action=manage&error=Feedback+not+found");
            return;
        }

        // Allow admin OR the rider who created the feedback to delete
        if (!(user instanceof Admin) && !user.getId().equals(feedback.getRiderId())) {
            resp.sendRedirect("feedback?action=manage&error=Unauthorized+access");
            return;
        }

        boolean deleted = FeedbackDAO.deleteFeedback(feedbackId);
        if (deleted) {
            // Redirect based on user role
            if (user instanceof Admin) {
                resp.sendRedirect("feedback?action=manage&msg=Feedback+deleted+successfully");
            } else {
                resp.sendRedirect("feedback?msg=Feedback+deleted+successfully");
            }
        } else {
            resp.sendRedirect("feedback?action=manage&error=Failed+to+delete+feedback");
        }
    }
}