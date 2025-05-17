package com.app.bikerental.util;

import com.app.bikerental.model.Ride;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RideUtil {

    public static List<Ride> matchSharedRides(List<Ride> rides, String pickup, String drop) {
        if (rides == null || pickup == null || drop == null) {
            return Collections.emptyList();
        }

        return rides.stream()
                .filter(r -> r != null)
                .filter(r -> r.getPickupLocation() != null &&
                        r.getPickupLocation().equalsIgnoreCase(pickup.trim()))
                .filter(r -> r.getDropLocation() != null &&
                        r.getDropLocation().equalsIgnoreCase(drop.trim()))
                .filter(r -> r.getStatus().equals(Ride.STATUS_BOOKED))
                .filter(r -> r.getAvailableSeats() > 0)
                .collect(Collectors.toList());
    }

    public static String getFormattedTimestamp(String timestamp) {
        // Implement timestamp formatting if needed
        return timestamp;
    }
}