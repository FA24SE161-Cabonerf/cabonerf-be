package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.EmailVerificationToken;
import com.example.caboneftbe.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    EmailVerificationToken findByToken(String token);

    EmailVerificationToken findByUsers(Users users);
}
