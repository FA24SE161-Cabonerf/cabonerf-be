package com.example.cabonerfbe.util;

import com.example.cabonerfbe.exception.CustomExceptions;

import java.security.SecureRandom;

/**
 * The class Password generator.
 *
 * @author SonPHH.
 */
public class PasswordGenerator {
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+";
    private static final String ALL_CHARACTERS = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARACTERS;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generate random password method.
     *
     * @param length the length
     * @return the string
     */
    public static String generateRandomPassword(int length) {
        if (length < 1) throw CustomExceptions.unauthorized("Password length must be at least 1");

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(ALL_CHARACTERS.length());
            password.append(ALL_CHARACTERS.charAt(randomIndex));
        }
        return password.toString();
    }
}
