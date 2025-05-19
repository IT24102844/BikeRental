package com.app.bikerental.controller;

import com.app.bikerental.util.PaymentUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/deletePayment")
public class DeletePaymentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String last4 = request.getParameter("cardLast4");

        PaymentUtil.deletePaymentByCard(last4);

        response.sendRedirect("viewPayments");
    }
}
