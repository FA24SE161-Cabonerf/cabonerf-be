package com.example.cabonerfbe.util;

import java.util.UUID;

/**
 * The class Uuid util.
 *
 * @author SonPHH.
 */
public class UUIDUtil {
    /**
     * Generate uuid method.
     *
     * @return the string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate uuid without dashes method.
     *
     * @return the string
     */
    public static String generateUUIDWithoutDashes() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * From string method.
     *
     * @param uuidString the uuid string
     * @return the uuid
     */
    public static UUID fromString(String uuidString) {
        return UUID.fromString(uuidString);
    }
}
