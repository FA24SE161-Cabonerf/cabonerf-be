package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface Refresh token repository.
 *
 * @author SonPHH.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    /**
     * Find by token method.
     *
     * @param token the token
     * @return the optional
     */
    @Query("SELECT t FROM RefreshToken t where t.token like ?1 order by t.createdAt desc LIMIT 1")
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find top by token order by created at desc method.
     *
     * @param userId the user id
     * @return the optional
     */
    @Query("select t from RefreshToken t where t.isValid = true and t.users.id = ?1 order by t.createdAt desc LIMIT  1")
    Optional<RefreshToken> findTopByTokenOrderByCreatedAtDesc(UUID userId);

    /**
     * Find token by user method.
     *
     * @param id the id
     * @return the optional
     */
    @Query("SELECT t FROM RefreshToken t WHERE t.isValid = true and t.users.id = ?1")
    Optional<RefreshToken> findTokenByUser(UUID id);
}
