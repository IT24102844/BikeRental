package com.app.bikerental.dao;

import com.app.bikerental.model.*;
import java.io.*;
import java.util.*;

public class UserDAO {
    private static final String FILE_PATH = "data/users.txt";

    // CREATE
    public static void registerUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = String.join(",",
                    user.getId(), user.getName(), user.getUsername(),
                    user.getPassword(), user.getEmail(), user.getRole());
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // READ (by username)
    public static User getUserByUsername(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6 && data[2].equals(username)) {
                    return createUserObject(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ (by ID)
    public static User getUserById(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6 && data[0].equals(id)) {
                    return createUserObject(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public static boolean updateUser(User updatedUser) {
        List<String> users = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(updatedUser.getId())) {
                    users.add(String.join(",",
                            updatedUser.getId(), updatedUser.getName(), updatedUser.getUsername(),
                            updatedUser.getPassword(), updatedUser.getEmail(), updatedUser.getRole()));
                    updated = true;
                } else {
                    users.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String u : users) {
                bw.write(u);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return updated;
    }

    // DELETE
    public static boolean deleteUser(String id) {
        List<String> users = new ArrayList<>();
        boolean deleted = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(id)) {
                    users.add(line);
                } else {
                    deleted = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String u : users) {
                bw.write(u);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deleted;
    }

    // Factory method to create proper User object
    private static User createUserObject(String[] data) {
        String id = data[0];
        String name = data[1];
        String username = data[2];
        String password = data[3];
        String email = data[4];
        String role = data[5];

        return switch (role) {
            case "Rider" -> new Rider(id, name, username, password, email);
            case "Driver" -> new Driver(id, name, username, password, email);
            case "Admin" -> new Admin(id, name, username, password, email);
            default -> null;
        };
    }

    // In UserDAO.java
    public static boolean addUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(userToString(user));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String userToString(User user) {
        return String.join(",",
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    User user = createUserFromParts(parts);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static User createUserFromParts(String[] parts) {
        String id = parts[0];
        String name = parts[1];
        String username = parts[2];
        String password = parts[3];
        String email = parts[4];
        String role = parts[5];

        switch (role) {
            case "Admin":
                return new Admin(id, name, username, password, email);
            case "Driver":
                return new Driver(id, name, username, password, email);
            case "Rider":
                return new Rider(id, name, username, password, email);
            default:
                return null;
        }
    }
}
