package com.app.bikerental.dao;

import com.app.bikerental.model.Feedback;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FeedbackDAO {
    private static final String FILE = "data/feedbacks.txt";

    public static boolean addFeedback(Feedback feedback) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(String.join("|",
                    feedback.getId(),
                    feedback.getRideId(),
                    feedback.getRiderId(),
                    feedback.getDriverId(), // Make sure this isn't null
                    String.valueOf(feedback.getRating()),
                    feedback.getComment(),
                    feedback.getTimestamp().toString()
            ));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Feedback> getFeedbacksByRider(String riderId) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 7 && parts[2].equals(riderId)) {
                    Feedback fb = new Feedback(
                            parts[0], parts[1], parts[2], parts[3],
                            Integer.parseInt(parts[4]), parts[5]
                    );
                    feedbacks.add(fb);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    public static List<Feedback> getFeedbacksByDriver(String driverId) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1); // Note: -1 keeps empty fields
                if (parts.length >= 7) {
                    // Handle empty driverId case (parts[3])
                    String feedbackDriverId = parts[3].isEmpty() ? "" : parts[3];
                    if (feedbackDriverId.equals(driverId)) {
                        Feedback fb = new Feedback(
                                parts[0], parts[1], parts[2], feedbackDriverId,
                                Integer.parseInt(parts[4]), parts[5]
                        );
                        feedbacks.add(fb);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    public static Optional<Feedback> getFeedbackByRide(String rideId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7 && parts[1].equals(rideId)) {
                    return Optional.of(new Feedback(
                            parts[0], parts[1], parts[2], parts[3],
                            Integer.parseInt(parts[4]), parts[5]
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Get all feedbacks (for admin)
    public static List<Feedback> getAllFeedbacks() {
        List<Feedback> feedbacks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 7) {
                    Feedback fb = new Feedback(
                            parts[0], parts[1], parts[2], parts[3],
                            Integer.parseInt(parts[4]), parts[5]
                    );
                    feedbacks.add(fb);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    // Search feedbacks (for admin)
    public static List<Feedback> searchFeedbacks(String searchTerm) {
        List<Feedback> allFeedbacks = getAllFeedbacks();
        String lowerSearch = searchTerm.toLowerCase();

        return allFeedbacks.stream()
                .filter(fb ->
                        fb.getId().toLowerCase().contains(lowerSearch) ||
                                fb.getRideId().toLowerCase().contains(lowerSearch) ||
                                fb.getRiderId().toLowerCase().contains(lowerSearch) ||
                                fb.getDriverId().toLowerCase().contains(lowerSearch) ||
                                fb.getComment().toLowerCase().contains(lowerSearch) ||
                                String.valueOf(fb.getRating()).contains(lowerSearch))
                .collect(Collectors.toList());
    }

    public static boolean deleteFeedback(String feedbackId) {
        // Read all feedbacks
        List<Feedback> allFeedbacks = getAllFeedbacks();

        // Find and remove the feedback with matching ID
        boolean removed = allFeedbacks.removeIf(fb -> fb.getId().equals(feedbackId));

        if (removed) {
            try {
                // Rewrite the entire file without the deleted feedback
                BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
                for (Feedback fb : allFeedbacks) {
                    bw.write(fb.toString());
                    bw.newLine();
                }
                bw.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static Feedback getFeedbackById(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7 && parts[0].equals(id)) {
                    return new Feedback(
                            parts[0], parts[1], parts[2], parts[3],
                            Integer.parseInt(parts[4]), parts[5]
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}