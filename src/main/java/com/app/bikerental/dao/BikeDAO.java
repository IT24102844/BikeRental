package com.app.bikerental.dao;

import com.app.bikerental.model.*;
import com.app.bikerental.util.IDGenerator;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BikeDAO {
    private static final String FILE = "data/bikes.txt";

    public static boolean addBike(Bike bike) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(bike.toString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Bike> getAllBikes() {
        List<Bike> bikes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Bike bike = parseBike(line);
                if (bike != null) bikes.add(bike);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bikes;
    }

    public static Bike getBikeById(String id) {
        return getAllBikes().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst().orElse(null);
    }

    public static boolean updateBike(Bike updatedBike) {
        List<Bike> bikes = getAllBikes();
        boolean found = false;

        for (int i = 0; i < bikes.size(); i++) {
            if (bikes.get(i).getId().equals(updatedBike.getId())) {
                bikes.set(i, updatedBike);
                found = true;
                break;
            }
        }

        if (!found) return false;

        return saveAll(bikes);
    }

    public static boolean deleteBike(String id) {
        List<Bike> bikes = getAllBikes();
        boolean removed = bikes.removeIf(b -> b.getId().equals(id));
        if (!removed) return false;
        return saveAll(bikes);
    }

    private static boolean saveAll(List<Bike> bikes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Bike bike : bikes) {
                bw.write(bike.toString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Bike parseBike(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length < 7) return null;

            String id = parts[0];
            String model = parts[1];
            String condition = parts[2];
            boolean available = Boolean.parseBoolean(parts[3]);
            String driverId = parts[4];
            String availability = parts[5];
            String type = parts[6];

            return switch (type) {
                case "Electric" -> new ElectricBike(id, model, condition, available, driverId, availability);
                case "Manual" -> new ManualBike(id, model, condition, available, driverId, availability);
                default -> null;
            };
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String generateBikeId() {
        return IDGenerator.generateId("B");
    }
}
