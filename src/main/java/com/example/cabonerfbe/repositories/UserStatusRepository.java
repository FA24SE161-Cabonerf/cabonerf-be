package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
    @Query("SELECT s FROM UserStatus s WHERE s.statusName like ?1")
    Optional<UserStatus> findUserStatusByName(String name);
}
