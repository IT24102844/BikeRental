package com.app.bikerental.model;

public class ElectricBike extends Bike {
    public ElectricBike(String id, String model, String condition, boolean available, String driverId, String availability) {
        super(id, model, condition, available, driverId, availability);
    }

    @Override
    public String getType() {
        return "Electric";
    }
}
