package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Midpoint impact characterization factors repository.
 *
 * @author SonPHH.
 */
@Repository
public interface MidpointImpactCharacterizationFactorsRepository extends JpaRepository<MidpointImpactCharacterizationFactors, UUID> {

    /**
     * Check exist method.
     *
     * @param substanceCompartmentId the substance compartment id
     * @param impactMethodCategoryId the impact method category id
     * @return the optional
     */
    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f " +
            "JOIN FETCH f.emissionSubstance sc " +
            "JOIN FETCH f.impactMethodCategory imc " +
            "WHERE sc.id = ?1 AND imc.id = ?2 AND f.status = true")
    Optional<MidpointImpactCharacterizationFactors> checkExist(UUID substanceCompartmentId, UUID impactMethodCategoryId);


    /**
     * Find by substance compartment and method with join fetch method.
     *
     * @param scId     the sc id
     * @param methodId the method id
     * @return the list
     */
    @Query("SELECT DISTINCT f FROM MidpointImpactCharacterizationFactors f " +
            "LEFT JOIN FETCH f.impactMethodCategory " +
            "WHERE f.emissionSubstance.id = :scId AND f.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = :methodId")
    List<MidpointImpactCharacterizationFactors> findBySubstanceCompartmentAndMethodWithJoinFetch(
            @Param("scId") UUID scId,
            @Param("methodId") UUID methodId);

    /**
     * Find by substance compartment and method and category with join fetch method.
     *
     * @param scId       the sc id
     * @param methodId   the method id
     * @param categoryId the category id
     * @return the list
     */
    @Query("SELECT DISTINCT f FROM MidpointImpactCharacterizationFactors f " +
            "LEFT JOIN FETCH f.impactMethodCategory " +
            "WHERE f.emissionSubstance.id = :scId " +
            "AND f.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = :methodId " +
            "AND f.impactMethodCategory.impactCategory.id = :categoryId")
    List<MidpointImpactCharacterizationFactors> findBySubstanceCompartmentAndMethodAndCategoryWithJoinFetch(
            @Param("scId") UUID scId,
            @Param("methodId") UUID methodId, @Param("categoryId") UUID categoryId);


    /**
     * Check exist create method.
     *
     * @param substanceCompartmentId the substance compartment id
     * @param methodId               the method id
     * @param categoryId             the category id
     * @return the optional
     */
    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f " +
            "JOIN FETCH f.emissionSubstance sc " +
            "JOIN FETCH f.impactMethodCategory imc " +
            "WHERE sc.id = ?1 AND imc.lifeCycleImpactAssessmentMethod.id = ?2 AND imc.impactCategory.id = ?3 AND f.status = true")
    Optional<MidpointImpactCharacterizationFactors> checkExistCreate(UUID substanceCompartmentId, UUID methodId, UUID categoryId);

    /**
     * Find by method category and emission substance method.
     *
     * @param methodCategoryId    the method category id
     * @param emissionSubstanceId the emission substance id
     * @return the optional
     */
    @Query("""
            select m from MidpointImpactCharacterizationFactors m
            where m.impactMethodCategory.id = ?1 and m.emissionSubstance.id = ?2 and m.status = true""")
    Optional<MidpointImpactCharacterizationFactors> findByMethodCategoryAndEmissionSubstance(UUID methodCategoryId, UUID emissionSubstanceId);

    /**
     * Find decimal value by method category and emission substance method.
     *
     * @param methodCategoryId    the method category id
     * @param emissionSubstanceId the emission substance id
     * @return the optional
     */
    @Query("""
            select m.decimalValue from MidpointImpactCharacterizationFactors m
            where m.impactMethodCategory.id = ?1 and m.emissionSubstance.id = ?2 and m.status = true""")
    Optional<BigDecimal> findDecimalValueByMethodCategoryAndEmissionSubstance(UUID methodCategoryId, UUID emissionSubstanceId);

    /**
     * Find by emission substance id method.
     *
     * @param emissionSubstanceId the emission substance id
     * @return the list
     */
    @Query("""
            select m from MidpointImpactCharacterizationFactors m
            where m.emissionSubstance.id = ?1 and m.status = true""")
    List<MidpointImpactCharacterizationFactors> findByEmissionSubstanceId(UUID emissionSubstanceId);
}
