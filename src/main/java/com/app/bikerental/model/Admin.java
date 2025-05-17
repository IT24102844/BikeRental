package com.app.bikerental.model;

public class Admin extends User {

    public Admin(String id, String name, String username, String password, String email) {
        super(id, name, username, password, email, "Admin");
    }

    @Override
    public String getDashboard() {
        return "Admin Control Panel";
    }
}
