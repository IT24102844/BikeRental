package com.app.bikerental.model;

import java.util.List;

public class Payment {
    private String name;
    private List<String> courses;
    private double amount;
    private String cardNumber;
    private String status;

    public Payment(String name, List<String> courses, double amount, String cardNumber, String status) {
        this.name = name;
        this.courses = courses;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String formattedAmount = String.format("%.2f", getAmount());
        return "Name: " + getName() +
                ", Amount: " + formattedAmount +
                ", Card: ****" + getCardNumber().substring(getCardNumber().length() - 4) +
                ", Status: " + getStatus();
    }
}

