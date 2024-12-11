package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface User organization repository.
 *
 * @author SonPHH.
 */
@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, UUID> {
    /**
     * Find by user and organization method.
     *
     * @param organizationId the organization id
     * @param userId         the user id
     * @return the optional
     */
    @Query("SELECT uo FROM UserOrganization uo WHERE uo.organization.id = :organizationId AND uo.user.id = :userId")
    Optional<UserOrganization> findByUserAndOrganization(@Param("organizationId") UUID organizationId, @Param("userId") UUID userId);

    /**
     * Find by user method.
     *
     * @param userId the user id
     * @return the list
     */
    @Query("SELECT uo FROM UserOrganization uo WHERE uo.user.id = :userId AND uo.hasJoined = true AND uo.status = true")
    List<UserOrganization> findByUser(@Param("userId") UUID userId);

    /**
     * Gets by user.
     *
     * @param userId the user id
     * @return the by user
     */
    @Query("SELECT uo FROM UserOrganization uo WHERE uo.user.id = :userId AND uo.status = true AND uo.hasJoined = true ORDER BY uo.createdAt DESC")
    List<UserOrganization> getByUser(@Param("userId") UUID userId);

    /**
     * Find invite method.
     *
     * @param userIds the user ids
     * @param id      the id
     * @return the list
     */
    @Query("SELECT uo FROM UserOrganization uo WHERE uo.user.id IN :userIds AND uo.organization.id = :organizationId AND uo.status = true")
    List<UserOrganization> findInvite(@Param("userIds") List<UUID> userIds, @Param("organizationId") UUID id);

    /**
     * Find by organization method.
     *
     * @param organizationId the organization id
     * @return the list
     */
    @Query("SELECT uo FROM UserOrganization uo WHERE uo.organization.id = :organizationId AND uo.status = true ")
    List<UserOrganization> findByOrganization(@Param("organizationId") UUID organizationId);
}
