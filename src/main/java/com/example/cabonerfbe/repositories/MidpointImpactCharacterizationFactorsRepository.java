package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MidpointImpactCharacterizationFactorsRepository extends JpaRepository<MidpointImpactCharacterizationFactors, UUID> {

    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f " +
            "JOIN FETCH f.substancesCompartments sc " +
            "JOIN FETCH f.impactMethodCategory imc " +
            "WHERE sc.id = ?1 AND imc.id = ?2 AND f.status = true")
    Optional<MidpointImpactCharacterizationFactors> checkExist(UUID substanceCompartmentId, UUID impactMethodCategoryId);


    @Query("SELECT DISTINCT f FROM MidpointImpactCharacterizationFactors f " +
            "LEFT JOIN FETCH f.impactMethodCategory " +
            "WHERE f.substancesCompartments.id = :scId AND f.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = :methodId")
    List<MidpointImpactCharacterizationFactors> findBySubstanceCompartmentAndMethodWithJoinFetch(
            @Param("scId") UUID scId,
            @Param("methodId") UUID methodId);

    @Query("SELECT DISTINCT f FROM MidpointImpactCharacterizationFactors f " +
            "LEFT JOIN FETCH f.impactMethodCategory " +
            "WHERE f.substancesCompartments.id = :scId " +
            "AND f.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = :methodId " +
            "AND f.impactMethodCategory.impactCategory.id = :categoryId")
    List<MidpointImpactCharacterizationFactors> findBySubstanceCompartmentAndMethodAndCategoryWithJoinFetch(
            @Param("scId") UUID scId,
            @Param("methodId") UUID methodId, @Param("categoryId") UUID categoryId);


    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f " +
            "JOIN FETCH f.substancesCompartments sc " +
            "JOIN FETCH f.impactMethodCategory imc " +
            "WHERE sc.id = ?1 AND imc.lifeCycleImpactAssessmentMethod.id = ?2 AND imc.impactCategory.id = ?3 AND f.status = true")
    Optional<MidpointImpactCharacterizationFactors> checkExistCreate(UUID substanceCompartmentId, UUID methodId, UUID categoryId);
}
