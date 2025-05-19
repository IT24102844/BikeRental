package com.app.bikerental.controller;

import com.app.bikerental.dao.BikeDAO;
import com.app.bikerental.dao.UserDAO;
import com.app.bikerental.model.*;
import com.app.bikerental.util.BikeRequestQueue;
import com.app.bikerental.util.IDGenerator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
    private static final BikeRequestQueue requestQueue = new BikeRequestQueue();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isAdminUser(req)) {
            resp.sendRedirect("login.jsp?error=Unauthorized+access");
            return;
        }

        String action = req.getParameter("action");

        if (action == null) {
            showDashboard(req, resp);
            return;
        }

        try {
            switch (action) {
                case "dashboard":
                    showDashboard(req, resp);
                    break;
                case "deleteUser":
                    deleteUser(req, resp);
                    break;
                case "deleteBike":
                    deleteBike(req, resp);
                    break;
                case "processNextRequest":
                    processNextRequest(req, resp);
                    break;
                case "clearQueue":
                    clearQueue(req, resp);
                    break;
                case "processRequest":
                    processSpecificRequest(req, resp);
                    break;
                default:
                    resp.sendRedirect("admin?action=dashboard");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("admin?action=dashboard&error=Error+processing+request");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isAdminUser(req)) {
            resp.sendRedirect("login.jsp?error=Unauthorized+access");
            return;
        }

        String action = req.getParameter("action");

        try {
            switch (action) {
                case "addUser":
                    addUser(req, resp);
                    break;
                case "updateUser":
                    updateUser(req, resp);
                    break;
                case "addBike":
                    addBike(req, resp);
                    break;
                case "updateBike":
                    updateBike(req, resp);
                    break;
                case "cancelRequest":
                    cancelRequest(req, resp);
                    break;
                default:
                    resp.sendRedirect("admin?action=dashboard");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("admin?action=dashboard&error=Error+processing+request");
        }
    }

    private boolean isAdminUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return false;
        }

        User user = (User) session.getAttribute("user");
        return "Admin".equals(user.getRole());
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> users = UserDAO.getAllUsers();
        List<Bike> bikes = BikeDAO.getAllBikes();
        List<BikeRequest> requests = getQueueContents();

        req.setAttribute("users", users);
        req.setAttribute("bikes", bikes);
        req.setAttribute("requests", requests);
        req.getRequestDispatcher("adminDashboard.jsp").forward(req, resp);
    }

    private List<BikeRequest> getQueueContents() {
        List<BikeRequest> requests = new ArrayList<>();
        BikeRequestQueue tempQueue = new BikeRequestQueue();

        synchronized (requestQueue) {
            while (!requestQueue.isEmpty()) {
                BikeRequest request = requestQueue.dequeue();
                requests.add(request);
                tempQueue.enqueue(request);
            }

            while (!tempQueue.isEmpty()) {
                requestQueue.enqueue(tempQueue.dequeue());
            }
        }

        return requests;
    }

    // In AdminController.java
    private void addUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        User user;
        switch (role) {
            case "Admin":
                user = new Admin(IDGenerator.generateId("U"), name, username, password, email);
                break;
            case "Driver":
                user = new Driver(IDGenerator.generateId("U"), name, username, password, email);
                break;
            case "Rider":
            default:
                user = new Rider(IDGenerator.generateId("U"), name, username, password, email);
                break;
        }

        boolean success = UserDAO.addUser(user);

        if (success) {
            resp.sendRedirect("admin?action=dashboard&msg=User+added+successfully");
        } else {
            resp.sendRedirect("admin?action=dashboard&error=Failed+to+add+user");
        }
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String userId = req.getParameter("userId");
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        User user;
        switch (role) {
            case "Admin":
                user = new Admin(userId, name, username, password, email);
                break;
            case "Driver":
                user = new Driver(userId, name, username, password, email);
                break;
            case "Rider":
            default:
                user = new Rider(userId, name, username, password, email);
                break;
        }

        boolean success = UserDAO.updateUser(user);

        if (success) {
            resp.sendRedirect("admin?action=dashboard&msg=User+updated+successfully");
        } else {
            resp.sendRedirect("admin?action=dashboard&error=Failed+to+update+user");
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String userId = req.getParameter("userId");
        boolean success = UserDAO.deleteUser(userId);

        if (success) {
            resp.sendRedirect("admin?action=dashboard&msg=User+deleted+successfully");
        } else {
            resp.sendRedirect("admin?action=dashboard&error=Failed+to+delete+user");
        }
    }

    private void addBike(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String model = req.getParameter("model");
        String type = req.getParameter("type");
        String condition = req.getParameter("condition");
        String driverId = req.getParameter("driverId");
        String availability = req.getParameter("availability");

        Bike bike;
        if ("Electric".equalsIgnoreCase(type)) {
            bike = new ElectricBike(IDGenerator.generateId("B"), model, condition,
                    "Available".equals(availability), driverId, availability);
        } else {
            bike = new ManualBike(IDGenerator.generateId("B"), model, condition,
                    "Available".equals(availability), driverId, availability);
        }

        boolean success = BikeDAO.addBike(bike);

        if (success) {
            resp.sendRedirect("admin?action=dashboard&msg=Bike+added+successfully");
        } else {
            resp.sendRedirect("admin?action=dashboard&error=Failed+to+add+bike");
        }
    }

    private void updateBike(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String bikeId = req.getParameter("bikeId");
        String model = req.getParameter("model");
        String type = req.getParameter("type");
        String condition = req.getParameter("condition");
        String driverId = req.getParameter("driverId");
        String availability = req.getParameter("availability");

        Bike bike = BikeDAO.getBikeById(bikeId);
        if (bike != null) {
            bike.setModel(model);
            bike.setCondition(condition);
            bike.setDriverId(driverId);
            bike.setAvailability(availability);
            bike.setAvailable("Available".equals(availability));

            boolean success = BikeDAO.updateBike(bike);

            if (success) {
                resp.sendRedirect("admin?action=dashboard&msg=Bike+updated+successfully");
            } else {
                resp.sendRedirect("admin?action=dashboard&error=Failed+to+update+bike");
            }
        } else {
            resp.sendRedirect("admin?action=dashboard&error=Bike+not+found");
        }
    }

    private void deleteBike(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String bikeId = req.getParameter("bikeId");
        boolean success = BikeDAO.deleteBike(bikeId);

        if (success) {
            resp.sendRedirect("admin?action=dashboard&msg=Bike+deleted+successfully");
        } else {
            resp.sendRedirect("admin?action=dashboard&error=Failed+to+delete+bike");
        }
    }

    private void processNextRequest(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        BikeRequest request = requestQueue.dequeue();
        if (request != null) {
            processRequest(request);
            resp.sendRedirect("admin?action=dashboard&msg=Request+processed+successfully");
        } else {
            resp.sendRedirect("admin?action=dashboard&error=No+requests+in+queue");
        }
    }

    private void clearQueue(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        requestQueue.clear();
        resp.sendRedirect("admin?action=dashboard&msg=Queue+cleared+successfully");
    }

    private void processSpecificRequest(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String requestId = req.getParameter("requestId");
        BikeRequest targetRequest = null;
        BikeRequestQueue tempQueue = new BikeRequestQueue();

        synchronized (requestQueue) {
            while (!requestQueue.isEmpty()) {
                BikeRequest request = requestQueue.dequeue();
                if (request.getRequestId().equals(requestId)) {
                    targetRequest = request;
                } else {
                    tempQueue.enqueue(request);
                }
            }

            while (!tempQueue.isEmpty()) {
                requestQueue.enqueue(tempQueue.dequeue());
            }
        }

        if (targetRequest != null) {
            processRequest(targetRequest);
            resp.sendRedirect("admin?action=dashboard&msg=Request+processed+successfully");
        } else {
            resp.sendRedirect("admin?action=dashboard&error=Request+not+found");
        }
    }

    private void cancelRequest(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String requestId = req.getParameter("requestId");
        boolean found = false;
        BikeRequestQueue tempQueue = new BikeRequestQueue();

        synchronized (requestQueue) {
            while (!requestQueue.isEmpty()) {
                BikeRequest request = requestQueue.dequeue();
                if (request.getRequestId().equals(requestId)) {
                    request.setStatus("CANCELLED");
                    found = true;
                }
                tempQueue.enqueue(request);
            }

            while (!tempQueue.isEmpty()) {
                requestQueue.enqueue(tempQueue.dequeue());
            }
        }

        if (found) {
            resp.sendRedirect("admin?action=dashboard&msg=Request+cancelled+successfully");
        } else {
            resp.sendRedirect("admin?action=dashboard&error=Request+not+found");
        }
    }

    private void processRequest(BikeRequest request) {
        try {
            request.setStatus("PROCESSING");

            Bike bike = BikeDAO.getBikeById(request.getBikeId());
            if (bike == null || !bike.isAvailable()) {
                request.setStatus("CANCELLED");
                return;
            }

            // Here you would create the ride and update bike status
            // Similar to your existing ride creation logic

            request.setStatus("COMPLETED");
        } catch (Exception e) {
            e.printStackTrace();
            request.setStatus("CANCELLED");
        }
    }
}