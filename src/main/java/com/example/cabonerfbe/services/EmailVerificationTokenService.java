package com.example.cabonerfbe.services;

import com.example.cabonerfbe.models.EmailVerificationToken;
import com.example.cabonerfbe.models.Users;

import java.sql.Timestamp;

public interface EmailVerificationTokenService {

    EmailVerificationToken findByToken(String token);
    EmailVerificationToken findByUser(Users users);
    void save(Users users, String token);
    Timestamp calculateExpiryDate(int expiryTimeInMinutes);
}
