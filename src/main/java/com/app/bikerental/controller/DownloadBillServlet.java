package com.app.bikerental.controller;

import com.app.bikerental.model.Payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/downloadBill")
public class DownloadBillServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Payment payment = (Payment) request.getSession().getAttribute("latestPayment");

        if (payment == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No payment found.");
            return;
        }

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=Payment_Bill.txt");
        response.getWriter().write(payment.toString());
    }
}
