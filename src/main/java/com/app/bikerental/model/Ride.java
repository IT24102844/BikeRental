package com.app.bikerental.model;

public class Ride {
    private String rideId;
    private String riderId;
    private String driverId;
    private String bikeId;
    private String pickupLocation;
    private String dropLocation;
    private String status;
    private String timestamp;

    // Status constants
    public static final String STATUS_BOOKED = "Booked";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";

    public Ride() {}

    public Ride(String rideId, String riderId, String driverId, String bikeId, String pickupLocation,
                String dropLocation, String status, String timestamp) {
        this.rideId = rideId;
        this.riderId = riderId;
        this.driverId = driverId;
        this.bikeId = bikeId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.join(",",
                rideId, riderId, driverId, bikeId,
                pickupLocation, dropLocation,
                status, timestamp
        );
    }

}