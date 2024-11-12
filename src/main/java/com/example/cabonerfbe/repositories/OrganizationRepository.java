package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Organization;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    @Query("SELECT o FROM Organization o WHERE UPPER(o.name) like UPPER(CONCAT('%',:keyword,'%')) AND o.status = true")
    Page<Organization> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @NotNull
    @Query("SELECT o FROM Organization o WHERE o.status = true")
    Page<Organization> findAll(@NotNull Pageable pageable);
    @NotNull
    @Query("SELECT o FROM Organization o WHERE o.id = :organizationId AND o.status = true")
    Optional<Organization> findById(@NotNull @Param("organizationId") UUID id);
}
