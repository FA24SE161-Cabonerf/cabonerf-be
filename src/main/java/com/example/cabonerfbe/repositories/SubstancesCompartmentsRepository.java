package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.SubstancesCompartments;
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
public interface SubstancesCompartmentsRepository extends JpaRepository<SubstancesCompartments, UUID> {

    @Query("SELECT sc FROM SubstancesCompartments sc WHERE sc.status = true")
    List<SubstancesCompartments> findAll();

    @Query("SELECT sc FROM SubstancesCompartments sc WHERE sc.id = :id AND sc.status = true")
    Optional<SubstancesCompartments> findById(@Param("id") UUID substanceCompartmentId);


    @Query("SELECT sc FROM SubstancesCompartments sc " +
            "JOIN FETCH sc.emissionSubstance es " +
            "JOIN FETCH sc.emissionCompartment ec " +
            "WHERE es.id = ?1 AND ec.id = ?2 AND sc.status = true")
    Optional<SubstancesCompartments> checkExistBySubstanceAndCompartment(UUID substanceId, UUID compartmentId);

    @Query("SELECT sc FROM SubstancesCompartments sc " +
            "WHERE sc.emissionSubstance.id = :emissionSubstanceId AND sc.status = true")
    Optional<SubstancesCompartments> checkExistBySubstance(@Param("emissionSubstanceId") UUID emissionSubstanceId);


    @Query("SELECT DISTINCT sc FROM SubstancesCompartments sc " +
            "LEFT JOIN FETCH sc.emissionSubstance " +
            "LEFT JOIN FETCH sc.emissionCompartment " +
            "WHERE sc.status = true")
    Page<SubstancesCompartments> findAllWithJoinFetch(Pageable pageable);

    @Query("SELECT DISTINCT sc FROM SubstancesCompartments sc " +
            "LEFT JOIN FETCH sc.emissionSubstance es " +
            "LEFT JOIN FETCH sc.emissionCompartment " +
            "WHERE sc.status = true AND LOWER(es.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(es.chemicalName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.molecularFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.alternativeFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SubstancesCompartments> searchByKeywordWithJoinFetch(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT sc FROM SubstancesCompartments sc " +
            "LEFT JOIN FETCH sc.emissionSubstance " +
            "LEFT JOIN FETCH sc.emissionCompartment ec " +
            "WHERE ec.id = :compartmentId AND sc.status = true")
    Page<SubstancesCompartments> searchByCompartmentWithJoinFetch(@Param("compartmentId") UUID compartmentId, Pageable pageable);

    @Query("SELECT DISTINCT sc FROM SubstancesCompartments sc " +
            "LEFT JOIN FETCH sc.emissionSubstance es " +
            "LEFT JOIN FETCH sc.emissionCompartment ec " +
            "WHERE sc.status = true AND ec.id = :compartmentId " +
            "AND (LOWER(es.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(es.chemicalName) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "OR LOWER(es.molecularFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(es.alternativeFormula) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SubstancesCompartments> searchBySubstanceAndCompartmentWithJoinFetch(
            @Param("keyword") String keyword,
            @Param("compartmentId") UUID compartmentId,
            Pageable pageable);
}

