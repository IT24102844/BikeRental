package com.app.bikerental.controller;

import com.app.bikerental.dao.UserDAO;
import com.app.bikerental.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/user")
public class UserController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("logout".equals(action)) {
            req.getSession().invalidate();
            resp.sendRedirect("login.jsp");
            return;
        }

        if ("delete".equals(action)) {
            String userId = req.getParameter("id");
            if (UserDAO.deleteUser(userId)) {
                req.getSession().invalidate();
                resp.sendRedirect("login.jsp?msg=Account deleted successfully");
            } else {
                resp.sendRedirect("profile.jsp?error=Failed to delete account");
            }
            return;
        }

        // Default: Show profile page if logged in
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            req.getRequestDispatcher("profile.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "register" -> handleRegister(req, resp);
            case "login" -> handleLogin(req, resp);
            case "updateProfile" -> handleUpdateProfile(req, resp);
            default -> resp.sendRedirect("login.jsp");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String role = req.getParameter("role"); // expect Rider, Driver, or Admin

        // Simple ID generation - can be improved or use your own IDGenerator utility
        String id = "U" + UUID.randomUUID().toString().substring(0, 8);

        User newUser;
        switch (role) {
            case "Rider" -> newUser = new Rider(id, name, username, password, email);
            case "Driver" -> newUser = new Driver(id, name, username, password, email);
            case "Admin" -> newUser = new Admin(id, name, username, password, email);
            default -> {
                resp.sendRedirect("register.jsp?error=Invalid role selected");
                return;
            }
        }

        UserDAO.registerUser(newUser);
        resp.sendRedirect("login.jsp?msg=Registration successful, please login.");
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = UserDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId()); // Store user ID in session

            // Role-based redirect
            if (user instanceof Rider) {
                resp.sendRedirect("ride?action=dashboard"); // Redirect to rider dashboard
            } else if (user instanceof Driver) {
                resp.sendRedirect("bike");
            } else if (user instanceof Admin) {
                resp.sendRedirect("adminDashboard.jsp");
            } else {
                resp.sendRedirect("profile.jsp");
            }
        } else {
            req.setAttribute("error", "Invalid username or password");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Update fields
        user.setName(name);
        user.setEmail(email);
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        if (UserDAO.updateUser(user)) {
            session.setAttribute("user", user);
            resp.sendRedirect("profile.jsp?msg=Profile updated successfully");
        } else {
            req.setAttribute("error", "Failed to update profile");
            req.getRequestDispatcher("profile.jsp").forward(req, resp);
        }
    }
}