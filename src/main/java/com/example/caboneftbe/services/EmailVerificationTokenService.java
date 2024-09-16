package com.example.caboneftbe.services;

import com.example.caboneftbe.models.EmailVerificationToken;
import com.example.caboneftbe.models.Users;

import java.sql.Timestamp;

public interface EmailVerificationTokenService {

    EmailVerificationToken findByToken(String token);
    EmailVerificationToken findByUser(Users users);
    void save(Users users, String token);
    Timestamp calculateExpiryDate(int expiryTimeInMinutes);
}
