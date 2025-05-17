package com.app.bikerental.util;

import java.util.UUID;

public class IDGenerator {

    /**
     * Generate a unique ID with a prefix and 8 random alphanumeric characters.
     * Example: U1a2b3c4d
     *
     * @param prefix The prefix string (e.g., "U" for user)
     * @return A unique ID string
     */
    public static String generateId(String prefix) {
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return prefix + randomPart;
    }
}
