package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.models.EmailVerificationToken;
import com.example.caboneftbe.models.Users;
import com.example.caboneftbe.repositories.EmailVerificationTokenRepository;
import com.example.caboneftbe.services.EmailVerificationTokenService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    @Autowired
    EmailVerificationTokenRepository emailVerificationTokenRepository;

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
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(token,calculateExpiryDate(24*60),users);
        emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Override
    public Timestamp calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }
}
