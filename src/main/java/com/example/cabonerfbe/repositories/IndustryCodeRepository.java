package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.IndustryCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Industry code repository.
 *
 * @author SonPHH.
 */
@Repository
public interface IndustryCodeRepository extends JpaRepository<IndustryCode, UUID> {
    /**
     * Find by id with status method.
     *
     * @param industryCodeId the industry code id
     * @return the optional
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.id = :industryCodeId AND ic.status = true")
    Optional<IndustryCode> findByIdWithStatus(@Param("industryCodeId") UUID industryCodeId);

    /**
     * Find all by ids method.
     *
     * @param industryCodeIds the industry code ids
     * @return the list
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.id in :industryCodeIds AND ic.status = true")
    List<IndustryCode> findAllByIds(@Param("industryCodeIds") List<UUID> industryCodeIds);

    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true ")
    Page<IndustryCode> findAll(Pageable pageable);

    /**
     * Find all by keyword method.
     *
     * @param keyword  the keyword
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true AND ic.name ILIKE CONCAT('%', :keyword ,'%')")
    Page<IndustryCode> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Find by code method.
     *
     * @param code the code
     * @return the optional
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.code like :code AND ic.status = true")
    Optional<IndustryCode> findByCode(@Param("code") String code);

    /**
     * Exists by code and id not method.
     *
     * @param code the code
     * @param id   the id
     * @return the boolean
     */
    boolean existsByCodeAndIdNot(String code, UUID id);

    /**
     * Find by status method.
     *
     * @return the list
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true")
    List<IndustryCode> findByStatus();

    /**
     * Find by keyword method.
     *
     * @param keyword the keyword
     * @return the list
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true AND ic.name ILIKE CONCAT('%', :keyword ,'%')")
    List<IndustryCode> findByKeyword(@Param("keyword") String keyword);

    /**
     * Find all by status method.
     *
     * @return the list
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true ")
    List<IndustryCode> findAllByStatus();

    /**
     * Find by id with status method.
     *
     * @param industryCodeId the industry code id
     * @return the optional
     */
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.id = :industryCodeId")
    Optional<IndustryCode> findByIdWithStatusToCreateProject(@Param("industryCodeId") UUID industryCodeId);
}
