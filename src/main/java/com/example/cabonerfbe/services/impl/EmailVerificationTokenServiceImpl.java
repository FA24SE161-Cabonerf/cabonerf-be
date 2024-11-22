package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.EmailVerificationToken;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.repositories.EmailVerificationTokenRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.services.EmailVerificationTokenService;
import com.example.cabonerfbe.services.JwtService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

@Service
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    @Autowired
    EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    @Transactional
    @Override
    public EmailVerificationToken findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token);
    }

    @Transactional
    @Override
    public EmailVerificationToken findByUser(Users users) {
        return emailVerificationTokenRepository.findByUsers(users);
    }

    @Transactional
    @Override
    public void save(Users users, String token) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(token,calculateExpiryDate(24*60),true,users);
        emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Override
    public Timestamp calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    @Override
    public String generateToken(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound("User not exist"));

        String token = jwtService.generateEmailVerifyToken(u);
        EmailVerificationToken _token = new EmailVerificationToken(token,null,true,u);
        emailVerificationTokenRepository.save(_token);
        return token;
    }
}
