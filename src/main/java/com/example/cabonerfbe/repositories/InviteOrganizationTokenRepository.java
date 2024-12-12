package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.InviteOrganizationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The interface Invite organization token repository.
 *
 * @author SonPHH.
 */
@Repository
public interface InviteOrganizationTokenRepository extends JpaRepository<InviteOrganizationToken, UUID> {
    /**
     * Find by token method.
     *
     * @param inviteToken the invite token
     * @return the invite organization token
     */
    @Query("SELECT t FROM InviteOrganizationToken t WHERE t.token like ?1")
    InviteOrganizationToken findByToken(String inviteToken);
}
