package com.app.bikerental.model;

public class ManualBike extends Bike {
    public ManualBike(String id, String model, String condition, boolean available, String driverId, String availability) {
        super(id, model, condition, available, driverId, availability);
    }

    @Override
    public String getType() {
        return "Manual";
    }
}
