package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.OrganizationIndustryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface OrganizationIndustryCodeRepository extends JpaRepository<OrganizationIndustryCode, UUID> {
    @Query("SELECT oic " +
            "FROM OrganizationIndustryCode oic " +
            "WHERE oic.organization.id = :organizationId " +
            "AND oic.industryCode.id = :industryCodeId " +
            "AND oic.status = true")
    Optional<OrganizationIndustryCode> findByOrganizationAndIndustryCode(@Param("organizationId") UUID organizationId,
                                                                         @Param("industryCodeId") UUID industryCodeId);

    @Query("SELECT oic FROM OrganizationIndustryCode oic WHERE oic.organization.id = :organizationId AND oic.status = true ")
    List<OrganizationIndustryCode> findByOrganization(@Param("organizationId") UUID organizationId);

    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true")
    List<OrganizationIndustryCode> findAllByStatus();
}
