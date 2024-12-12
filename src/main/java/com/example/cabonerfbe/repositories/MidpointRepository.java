package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.enums.QueryStrings;
import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Midpoint repository.
 *
 * @author SonPHH.
 */
public interface MidpointRepository extends JpaRepository<MidpointImpactCharacterizationFactors, UUID> {
    /**
     * Find all by status method.
     *
     * @param status the status
     * @return the list
     */
    List<MidpointImpactCharacterizationFactors> findAllByStatus(boolean status);

    /**
     * Find all with perspective method.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = QueryStrings.FIND_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVES,
            countQuery = QueryStrings.COUNT_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVE,
            nativeQuery = true)
    Page<Object[]> findAllWithPerspective(Pageable pageable);

    /**
     * Find by key word method.
     *
     * @param keyword  the keyword
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = QueryStrings.FIND_FACTOR_BY_KEYWORD,
            countQuery = QueryStrings.COUNT_FACTORS_BY_KEYWORD,
            nativeQuery = true)
    Page<Object[]> findByKeyWord(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Filter by compartment method.
     *
     * @param compartmentId the compartment id
     * @param pageable      the pageable
     * @return the page
     */
    @Query(value = QueryStrings.FIND_FACTOR_BY_COMPARTMENT,
            countQuery = QueryStrings.COUNT_FACTORS_BY_COMPARTMENT,
            nativeQuery = true)
    Page<Object[]> filterByCompartment(@Param("compartmentId") UUID compartmentId, Pageable pageable);

    /**
     * Find by key word and compartment id method.
     *
     * @param keyword       the keyword
     * @param compartmentId the compartment id
     * @param pageable      the pageable
     * @return the page
     */
    @Query(value = QueryStrings.FIND_FACTOR_BY_KEYWORD_AND_COMPARTMENT,
            countQuery = QueryStrings.COUNT_FACTORS_BY_KEYWORD_AND_COMPARTMENT,
            nativeQuery = true)
    Page<Object[]> findByKeyWordAndCompartmentId(@Param("keyword") String keyword, @Param("compartmentId") UUID compartmentId, Pageable pageable);

    /**
     * Gets when create.
     *
     * @param id the id
     * @return the when create
     */
    @Query(value = QueryStrings.FIND_MIDPOINT_SUBSTANCE_FACTORS, nativeQuery = true)
    List<Object[]> getWhenCreate(@Param("id") UUID id);

    /**
     * Find by id and status method.
     *
     * @param id     the id
     * @param status the status
     * @return the optional
     */
    Optional<MidpointImpactCharacterizationFactors> findByIdAndStatus(UUID id, boolean status);

    /**
     * Find all to export method.
     *
     * @param methodCategoryId the method category id
     * @return the list
     */
    @Query(value = QueryStrings.FIND_MIDPOINT_SUBSTANCE_FACTORS_TO_EXPORT, nativeQuery = true)
    List<Object[]> findAllToExport(@Param("methodCategoryId") UUID methodCategoryId);
}
