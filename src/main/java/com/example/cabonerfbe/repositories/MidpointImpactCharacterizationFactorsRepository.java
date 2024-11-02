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

//    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f WHERE f.impactMethodCategory.id = ?1 AND f.emissionSubstances.name like ?2 AND f.emissionCompartment.id = ?3 AND f.emissionSubstances.molecularFormula like ?4")
//    MidpointImpactCharacterizationFactors findByImpactMethodCategoryIdAndEmissionSubstancesName(long methodId, String name, long emissionCompartmentId, String molecular);
//
//    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f WHERE f.emissionSubstances.id = ?1")
//    List<MidpointImpactCharacterizationFactors> findByEmissionSubstancesId(long emissionSubstancesId);

    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f WHERE f.substancesCompartments.id = ?1 AND f.impactMethodCategory.id = ?2")
    Optional<MidpointImpactCharacterizationFactors> checkExist(UUID substanceCompartmentId, UUID impactMethodCategoryId);

    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f WHERE f.substancesCompartments.id = ?1 AND f.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = ?2")
    List<MidpointImpactCharacterizationFactors> searchByMethod(UUID substanceCompartmentId, UUID methodId);

    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f WHERE f.substancesCompartments.id = ?1 AND f.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = ?2 AND f.impactMethodCategory.impactCategory.id = ?3")
    List<MidpointImpactCharacterizationFactors> searchByMethodAndImpactCategory(UUID substanceCompartmentId, UUID methodId, UUID categoryId);

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
}
