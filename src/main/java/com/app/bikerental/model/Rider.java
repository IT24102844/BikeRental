package com.app.bikerental.model;

public class Rider extends User {

    public Rider(String id, String name, String username, String password, String email) {
        super(id, name, username, password, email, "Rider");
    }

    @Override
    public String getDashboard() {
        return "Rider Dashboard";
    }
}
