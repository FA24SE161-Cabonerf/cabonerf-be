package com.example.cabonerfbe.services;

import com.example.cabonerfbe.models.EmailVerificationToken;
import com.example.cabonerfbe.models.Users;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * The interface Email verification token service.
 *
 * @author SonPHH.
 */
public interface EmailVerificationTokenService {

    /**
     * Find by token method.
     *
     * @param token the token
     * @return the email verification token
     */
    EmailVerificationToken findByToken(String token);

    /**
     * Find by user method.
     *
     * @param users the users
     * @return the email verification token
     */
    EmailVerificationToken findByUser(Users users);

    /**
     * Save method.
     *
     * @param users the users
     * @param token the token
     */
    void save(Users users, String token);

    /**
     * Calculate expiry date method.
     *
     * @param expiryTimeInMinutes the expiry time in minutes
     * @return the timestamp
     */
    Timestamp calculateExpiryDate(int expiryTimeInMinutes);

    /**
     * Generate token method.
     *
     * @param userId the user id
     * @return the string
     */
    String generateToken(UUID userId);
}
