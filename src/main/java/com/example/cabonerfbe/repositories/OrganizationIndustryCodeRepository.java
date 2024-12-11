package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.OrganizationIndustryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Organization industry code repository.
 *
 * @author SonPHH.
 */
@Repository
public interface OrganizationIndustryCodeRepository extends JpaRepository<OrganizationIndustryCode, UUID> {
    /**
     * Find by organization and industry code method.
     *
     * @param organizationId the organization id
     * @param industryCodeId the industry code id
     * @return the optional
     */
    @Query("SELECT oic " +
            "FROM OrganizationIndustryCode oic " +
            "WHERE oic.organization.id = :organizationId " +
            "AND oic.industryCode.id = :industryCodeId " +
            "AND oic.status = true")
    Optional<OrganizationIndustryCode> findByOrganizationAndIndustryCode(@Param("organizationId") UUID organizationId,
                                                                         @Param("industryCodeId") UUID industryCodeId);

    /**
     * Find by organization method.
     *
     * @param organizationId the organization id
     * @return the list
     */
    @Query("SELECT oic FROM OrganizationIndustryCode oic WHERE oic.organization.id = :organizationId AND oic.status = true ")
    List<OrganizationIndustryCode> findByOrganization(@Param("organizationId") UUID organizationId);

    /**
     * Find all by status method.
     *
     * @return the list
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true")
    List<OrganizationIndustryCode> findAllByStatus();
}
