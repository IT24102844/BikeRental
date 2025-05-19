package com.app.bikerental.model;

import java.time.LocalDateTime;

public class BikeRequest {
    private String requestId;
    private String userId;
    private String bikeId;
    private String bikeType; // "Electric" or "Manual"
    private LocalDateTime requestTime;
    private String status; // "PENDING", "PROCESSING", "COMPLETED", "CANCELLED"
    private String pickupLocation;
    private String dropLocation;

    public BikeRequest(String requestId, String userId, String bikeId, String bikeType,
                       String pickupLocation, String dropLocation) {
        this.requestId = requestId;
        this.userId = userId;
        this.bikeId = bikeId;
        this.bikeType = bikeType;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.requestTime = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and setters
    public String getRequestId() {
        return requestId;
    }

    public String getUserId() {
        return userId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getBikeType() {
        return bikeType;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public String getStatus() {
        return status;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BikeRequest{" +
                "requestId='" + requestId + '\'' +
                ", userId='" + userId + '\'' +
                ", bikeId='" + bikeId + '\'' +
                ", bikeType='" + bikeType + '\'' +
                ", requestTime=" + requestTime +
                ", status='" + status + '\'' +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", dropLocation='" + dropLocation + '\'' +
                '}';
    }
}