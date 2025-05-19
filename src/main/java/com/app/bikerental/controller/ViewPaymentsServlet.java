package com.app.bikerental.controller;

import com.app.bikerental.util.PaymentUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/viewPayments")
public class ViewPaymentsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> payments = PaymentUtil.getAllPayments();

        request.setAttribute("paymentList", payments);

        request.getRequestDispatcher("paymentHistory.jsp").forward(request, response);
    }
}
