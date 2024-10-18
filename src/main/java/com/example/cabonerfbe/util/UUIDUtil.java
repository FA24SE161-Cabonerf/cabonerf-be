package com.example.cabonerfbe.util;

import java.util.UUID;

public class UUIDUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateUUIDWithoutDashes() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
