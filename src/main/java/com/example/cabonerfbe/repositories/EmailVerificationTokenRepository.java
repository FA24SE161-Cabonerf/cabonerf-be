package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmailVerificationToken;
import com.example.cabonerfbe.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    EmailVerificationToken findByToken(String token);

    EmailVerificationToken findByUsers(Users users);
}
