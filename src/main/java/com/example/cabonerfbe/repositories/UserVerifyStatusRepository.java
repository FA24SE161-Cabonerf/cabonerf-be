package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UserVerifyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerifyStatusRepository extends JpaRepository<UserVerifyStatus, Long> {
}
