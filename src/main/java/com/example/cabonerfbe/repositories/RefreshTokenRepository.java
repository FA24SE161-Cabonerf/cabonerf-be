package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Query("SELECT t FROM RefreshToken t where t.token like ?1 order by t.createdAt desc LIMIT 1")
    Optional<RefreshToken> findByToken(String token);

    @Query("select t from RefreshToken t where t.isValid = true and t.users.id = ?1 order by t.createdAt desc LIMIT  1")
    Optional<RefreshToken> findTopByTokenOrderByCreatedAtDesc(UUID userId);

    @Query("SELECT t FROM RefreshToken t WHERE t.isValid = true and t.users.id = ?1")
    Optional<RefreshToken> findTokenByUser(UUID id);
}
