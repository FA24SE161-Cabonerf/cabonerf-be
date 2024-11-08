package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, UUID> {
}