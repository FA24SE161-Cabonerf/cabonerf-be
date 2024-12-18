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

/**
 * The interface Organization repository.
 *
 * @author SonPHH.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    /**
     * Find all by keyword method.
     *
     * @param keyword  the keyword
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT o FROM Organization o WHERE UPPER(o.name) like UPPER(CONCAT('%',:keyword,'%')) AND o.status = true AND o.contract is null")
    Page<Organization> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @NotNull
    @Query("SELECT o FROM Organization o WHERE o.status = true AND o.contract is not null")
    Page<Organization> findAll(@NotNull Pageable pageable);

    @NotNull
    @Query("SELECT o FROM Organization o WHERE o.id = :organizationId AND o.status = true")
    Optional<Organization> findById(@NotNull @Param("organizationId") UUID id);

    /**
     * Find by id when create method.
     *
     * @param id the id
     * @return the optional
     */
    @NotNull
    @Query("SELECT o FROM Organization o WHERE o.id = :organizationId")
    Optional<Organization> findByIdWhenCreate(@NotNull @Param("organizationId") UUID id);
}
