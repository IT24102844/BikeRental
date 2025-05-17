package com.app.bikerental.model;

public abstract class Bike {
    private String id;
    private String model;
    private String condition;
    private boolean available; // Optional: you may remove or keep
    private String driverId;
    private String availability;

    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_UNAVAILABLE = "Unavailable";
    public static final String STATUS_MAINTENANCE = "Maintenance";


    public Bike(String id, String model, String condition, boolean available, String driverId, String availability) {
        this.id = id;
        this.model = model;
        this.condition = condition;
        this.available = available;
        this.driverId = driverId;
        this.availability = availability;
    }

    public String getId() { return id; }
    public String getModel() { return model; }
    public String getCondition() { return condition; }
    public boolean isAvailable() { return available; }

    public String getDriverId() { return driverId; }
    public String getAvailability() { return availability; }

    public void setModel(String model) { this.model = model; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setAvailable(boolean available) { this.available = available; }

    public void setDriverId(String driverId) { this.driverId = driverId; }
    public void setAvailability(String availability) { this.availability = availability; }

    public abstract String getType();

    @Override
    public String toString() {
        return id + "," + model + "," + condition + "," + available + "," + driverId + "," + availability + "," + getType();
    }
}
