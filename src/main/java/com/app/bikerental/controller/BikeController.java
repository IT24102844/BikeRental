package com.app.bikerental.controller;

import com.app.bikerental.dao.BikeDAO;
import com.app.bikerental.model.*;

import com.app.bikerental.util.BikeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/bike")
public class BikeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            String id = req.getParameter("id");
            Bike bike = BikeDAO.getBikeById(id);
            req.setAttribute("bike", bike);
            req.getRequestDispatcher("edit_bike.jsp").forward(req, resp);
        } else if ("delete".equals(action)) {
            String id = req.getParameter("id");
            BikeDAO.deleteBike(id);
            resp.sendRedirect("driverDashboard.jsp?msg=Bike deleted");
        } else if ("sortAvailability".equals(action)) {
            List<Bike> bikes = BikeDAO.getAllBikes();
            BikeUtil.quickSortByAvailability(bikes, 0, bikes.size() - 1);
            req.setAttribute("bikes", bikes);
            req.getRequestDispatcher("driverDashboard.jsp").forward(req, resp);
        } else {
            List<Bike> bikes = BikeDAO.getAllBikes();
            BikeUtil.quickSortByAvailability(bikes, 0, bikes.size() - 1);
            req.setAttribute("bikes", bikes);
            req.getRequestDispatcher("driverDashboard.jsp").forward(req, resp);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            String model = req.getParameter("model");
            String condition = req.getParameter("condition");
            boolean available = "on".equals(req.getParameter("available"));
            String type = req.getParameter("type");

            String id = BikeDAO.generateBikeId();
            Bike bike;

            switch (type) {
                case "Electric" -> bike = new ElectricBike(id, model, condition, true, "", "Available");
                case "Manual" -> bike = new ManualBike(id, model, condition, true, "", "Available");
                default -> {
                    resp.sendRedirect("bike?error=Invalid bike type");
                    return;
                }
            }

            BikeDAO.addBike(bike);
            resp.sendRedirect("bike?msg=Bike added successfully");
        } else if ("update".equals(action)) {
            String id = req.getParameter("id");
            Bike existing = BikeDAO.getBikeById(id);
            if (existing == null) {
                resp.sendRedirect("bike?error=Bike not found");
                return;
            }

            existing.setModel(req.getParameter("model"));
            existing.setCondition(req.getParameter("condition"));
            existing.setAvailable("on".equals(req.getParameter("available")));

            BikeDAO.updateBike(existing);
            resp.sendRedirect("bike?msg=Bike updated");
        }
    }
}
