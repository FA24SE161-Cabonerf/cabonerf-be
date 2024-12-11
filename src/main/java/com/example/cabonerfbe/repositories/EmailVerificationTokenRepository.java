package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmailVerificationToken;
import com.example.cabonerfbe.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The interface Email verification token repository.
 *
 * @author SonPHH.
 */
@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {
    /**
     * Find by token method.
     *
     * @param token the token
     * @return the email verification token
     */
    EmailVerificationToken findByToken(String token);

    /**
     * Find by users method.
     *
     * @param users the users
     * @return the email verification token
     */
    EmailVerificationToken findByUsers(Users users);
}
