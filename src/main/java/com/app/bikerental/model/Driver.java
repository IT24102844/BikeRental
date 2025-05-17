package com.app.bikerental.model;

public class Driver extends User {

    public Driver(String id, String name, String username, String password, String email) {
        super(id, name, username, password, email, "Driver");
    }

    @Override
    public String getDashboard() {
        return "Driver Dashboard";
    }
}
