package com.app.bikerental.model;

import java.time.LocalDateTime;

public class Feedback {
    private String id;
    private String rideId;
    private String riderId;
    private String driverId;
    private int rating; // 1-5
    private String comment;
    private LocalDateTime timestamp;

    public Feedback(String id, String rideId, String riderId, String driverId,
                    int rating, String comment) {
        this.id = id;
        this.rideId = rideId;
        this.riderId = riderId;
        this.driverId = driverId == null ? "" : driverId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getRideId() { return rideId; }
    public String getRiderId() { return riderId; }
    public String getDriverId() { return driverId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.join("|",
                id, rideId, riderId, driverId,
                String.valueOf(rating), comment,
                timestamp.toString()
        );
    }
}