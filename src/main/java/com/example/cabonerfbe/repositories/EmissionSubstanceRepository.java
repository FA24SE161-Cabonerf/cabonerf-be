package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmissionSubstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmissionSubstanceRepository extends JpaRepository<EmissionSubstance, UUID> {

    @Query("SELECT sc FROM EmissionSubstance sc WHERE sc.status = true")
    List<EmissionSubstance> findAll();

    @Query("SELECT sc FROM EmissionSubstance sc WHERE sc.id = :id AND sc.status = true AND sc.isInput = :isInput")
    Optional<EmissionSubstance> findByIdWithInput(@Param("id") UUID substanceCompartmentId, @Param("isInput") boolean input);

    @Query("SELECT sc FROM EmissionSubstance sc WHERE sc.unit.id = :unitId AND sc.id = :scId AND sc.status = true ")
    Optional<EmissionSubstance> checkValidUnit(@Param("unitId") UUID unitId, @Param("scId") UUID scId);


    @Query("SELECT sc FROM EmissionSubstance sc " +
            "JOIN FETCH sc.substance es " +
            "JOIN FETCH sc.emissionCompartment ec " +
            "WHERE es.id = ?1 AND ec.id = ?2 AND sc.status = true")
    Optional<EmissionSubstance> checkExistBySubstanceAndCompartment(UUID substanceId, UUID compartmentId);

    @Query("SELECT sc FROM EmissionSubstance sc " +
            "WHERE sc.substance.id = :emissionSubstanceId AND sc.status = true")
    Optional<EmissionSubstance> checkExistBySubstance(@Param("emissionSubstanceId") UUID emissionSubstanceId);


    @Query("SELECT DISTINCT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance " +
            "LEFT JOIN FETCH sc.emissionCompartment " +
            "JOIN MidpointImpactCharacterizationFactors f ON f.emissionSubstance.id = sc.id " +
            "WHERE sc.status = true AND sc.isInput = :isInput")
    Page<EmissionSubstance> findAllWithJoinFetch(@Param("isInput") boolean input, Pageable pageable);

    @Query("SELECT DISTINCT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance es " +
            "LEFT JOIN FETCH sc.emissionCompartment " +
            "WHERE sc.status = true AND sc.isInput = :isInput " +
            "AND (LOWER(es.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(es.chemicalName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.molecularFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.alternativeFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.cas) like LOWER(CONCAT('%', :keyword, '%')))")
    Page<EmissionSubstance> searchByKeywordWithJoinFetch(@Param("isInput") boolean input, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance " +
            "LEFT JOIN FETCH sc.emissionCompartment ec " +
            "WHERE ec.id = :compartmentId AND sc.status = true AND sc.isInput = :isInput")
    Page<EmissionSubstance> searchByCompartmentWithJoinFetch(@Param("isInput") boolean input, @Param("compartmentId") UUID compartmentId, Pageable pageable);

    @Query("SELECT DISTINCT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance es " +
            "LEFT JOIN FETCH sc.emissionCompartment ec " +
            "WHERE sc.status = true AND sc.isInput = :isInput AND ec.id = :compartmentId " +
            "AND (LOWER(es.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(es.chemicalName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.molecularFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.alternativeFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.cas) like LOWER(CONCAT('%', :keyword, '%')))")
    Page<EmissionSubstance> searchBySubstanceAndCompartmentWithJoinFetch(
            @Param("isInput") boolean input,
            @Param("keyword") String keyword,
            @Param("compartmentId") UUID compartmentId,
            Pageable pageable);

    @Query("SELECT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance es " +
            "WHERE sc.status = true " +
            "AND (LOWER(es.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(es.chemicalName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.molecularFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.alternativeFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.cas) like LOWER(CONCAT('%', :keyword, '%')))")
    List<EmissionSubstance> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance " +
            "LEFT JOIN FETCH sc.emissionCompartment " +
            "JOIN MidpointImpactCharacterizationFactors f ON f.emissionSubstance.id = sc.id " +
            "WHERE sc.status = true AND sc.isInput = :isInput AND f.impactMethodCategory.impactCategory.id = :categoryId")
    Page<EmissionSubstance> findAllWithJoinFetchCategory(@Param("isInput") boolean input, @Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("SELECT DISTINCT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance es " +
            "LEFT JOIN FETCH sc.emissionCompartment " +
            "JOIN MidpointImpactCharacterizationFactors f ON f.emissionSubstance.id = sc.id " +
            "WHERE sc.status = true AND sc.isInput = :isInput AND f.impactMethodCategory.impactCategory.id = :categoryId " +
            "AND (LOWER(es.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(es.chemicalName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.molecularFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.alternativeFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.cas) like LOWER(CONCAT('%', :keyword, '%')))")
    Page<EmissionSubstance> searchByKeywordWithJoinFetchCategory(@Param("isInput") boolean input, @Param("keyword") String keyword, @Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("SELECT DISTINCT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance " +
            "LEFT JOIN FETCH sc.emissionCompartment ec " +
            "JOIN MidpointImpactCharacterizationFactors f ON f.emissionSubstance.id = sc.id " +
            "WHERE ec.id = :compartmentId AND sc.status = true AND sc.isInput = :isInput AND f.impactMethodCategory.impactCategory.id = :categoryId ")
    Page<EmissionSubstance> searchByCompartmentWithJoinFetchCategory(@Param("isInput") boolean input, @Param("compartmentId") UUID compartmentId, @Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("SELECT DISTINCT sc FROM EmissionSubstance sc " +
            "LEFT JOIN FETCH sc.substance es " +
            "LEFT JOIN FETCH sc.emissionCompartment ec " +
            "JOIN MidpointImpactCharacterizationFactors f ON f.emissionSubstance.id = sc.id " +
            "WHERE sc.status = true AND sc.isInput = :isInput AND ec.id = :compartmentId AND f.impactMethodCategory.impactCategory.id = :categoryId " +
            "AND (LOWER(es.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(es.chemicalName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.molecularFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.alternativeFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.cas) like LOWER(CONCAT('%', :keyword, '%')))")
    Page<EmissionSubstance> searchBySubstanceAndCompartmentWithJoinFetchCategory(
            @Param("isInput") boolean input,
            @Param("keyword") String keyword,
            @Param("compartmentId") UUID compartmentId,
            @Param("categoryId") UUID categoryId,
            Pageable pageable);

    @Query("SELECT count(es) FROM EmissionSubstance es WHERE es.emissionCompartment.id = :emissionCompartmentId")
    int findByCompartment(@Param("emissionCompartmentId") UUID emissionCompartmentId);
}

