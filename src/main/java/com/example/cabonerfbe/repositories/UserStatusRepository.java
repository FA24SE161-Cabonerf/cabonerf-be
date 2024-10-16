package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
}
