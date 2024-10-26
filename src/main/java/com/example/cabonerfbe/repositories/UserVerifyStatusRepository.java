package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UserVerifyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserVerifyStatusRepository extends JpaRepository<UserVerifyStatus, UUID> {
    @Query("SELECT s FROM UserVerifyStatus s WHERE s.statusName like ?1")
    Optional<UserVerifyStatus> findByName(String name);
}
