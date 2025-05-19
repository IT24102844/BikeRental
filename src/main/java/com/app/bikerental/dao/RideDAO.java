package com.app.bikerental.dao;

import com.app.bikerental.model.Ride;
import com.app.bikerental.util.IDGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RideDAO {
    private static final String FILE = "data/rides.txt";

    public static boolean addRide(Ride ride) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(ride.toString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Ride> getAllRides() {
        List<Ride> rides = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    rides.add(new Ride(
                            parts[0], parts[1], parts[2],    // rideId, riderId, bikeId
                            parts[3], parts[4],             // pickup, drop
                            parts[5], parts[6], parts[7]              // status, timestamp
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rides;
    }

    public static Optional<Ride> getRideById(String rideId) {
        return getAllRides().stream()
                .filter(r -> r.getRideId().equals(rideId))
                .findFirst();
    }

    public static List<Ride> getRidesByRider(String riderId) {
        return getAllRides().stream()
                .filter(r -> r.getRiderId().equals(riderId))
                .collect(Collectors.toList());
    }


    public static boolean updateRide(Ride updatedRide) {
        List<Ride> rides = getAllRides();
        for (int i = 0; i < rides.size(); i++) {
            if (rides.get(i).getRideId().equals(updatedRide.getRideId())) {
                rides.set(i, updatedRide);
                return saveAllRides(rides);
            }
        }
        return false;
    }

    public static boolean cancelRide(String rideId) {
        List<Ride> rides = getAllRides();
        boolean removed = rides.removeIf(r -> r.getRideId().equals(rideId));
        return removed && saveAllRides(rides);
    }

    private static boolean saveAllRides(List<Ride> rides) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Ride r : rides) {
                bw.write(r.toString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String generateRideId() {
        return IDGenerator.generateId("R");
    }
}