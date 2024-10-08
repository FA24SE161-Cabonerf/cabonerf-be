package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("SELECT t FROM RefreshToken t where t.token like ?1 order by t.createdAt desc LIMIT 1")
    Optional<RefreshToken> findByToken(String token);

    @Query("select t from RefreshToken t where t.isValid = true and t.users.id = ?1 order by t.createdAt desc LIMIT  1")
    Optional<RefreshToken> findTopByTokenOrderByCreatedAtDesc(long userId);

}
