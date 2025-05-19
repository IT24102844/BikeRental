package com.app.bikerental.controller;

import com.app.bikerental.model.Payment;
import com.app.bikerental.util.PaymentUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/makePayment")
public class MakePaymentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String cardNumber = request.getParameter("cardNumber");

        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            request.setAttribute("error", "Card number must be exactly 16 digits.");
            request.getRequestDispatcher("paymentForm.jsp").forward(request, response);
            return;
        }

        double amount = Double.parseDouble(request.getParameter("amount"));

        Payment payment = new Payment(name, List.of("General Payment"), amount, cardNumber, "Completed");
        PaymentUtil.savePayment(payment);

        request.setAttribute("name", name);
        request.setAttribute("amount", amount);

        request.getSession().setAttribute("latestPayment", payment);
        request.getRequestDispatcher("paymentSuccess.jsp").forward(request, response);
    }
}