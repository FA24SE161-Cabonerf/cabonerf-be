package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.InviteOrganizationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface InviteOrganizationTokenRepository extends JpaRepository<InviteOrganizationToken, UUID> {
    @Query("SELECT t FROM InviteOrganizationToken t WHERE t.token like ?1")
    InviteOrganizationToken findByToken(String inviteToken);
}
