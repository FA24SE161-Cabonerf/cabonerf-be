package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.UserVerifyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerifyStatusRepository extends JpaRepository<UserVerifyStatus, Long> {
}
