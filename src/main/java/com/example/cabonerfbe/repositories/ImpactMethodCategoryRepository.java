package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.ImpactCategory;
import com.example.cabonerfbe.models.ImpactMethodCategory;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Impact method category repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ImpactMethodCategoryRepository extends JpaRepository<ImpactMethodCategory, UUID> {
    /**
     * Find by impact category and impact method method.
     *
     * @param impactCategoryId the impact category id
     * @param impactMethodId   the impact method id
     * @return the impact method category
     */
    @Query("SELECT a FROM ImpactMethodCategory a " +
            "JOIN FETCH a.impactCategory ic " +
            "JOIN FETCH a.lifeCycleImpactAssessmentMethod lcim " +
            "WHERE ic.id = ?1 AND lcim.id = ?2")
    ImpactMethodCategory findByImpactCategoryAndImpactMethod(UUID impactCategoryId, UUID impactMethodId);

    /**
     * Find by method method.
     *
     * @param methodId the method id
     * @return the list
     */
    @Query("SELECT a FROM ImpactMethodCategory a WHERE a.lifeCycleImpactAssessmentMethod.id = ?1 AND a.status = true")
    List<ImpactMethodCategory> findByMethod(UUID methodId);

    /**
     * Find id by method method.
     *
     * @param methodId the method id
     * @return the list
     */
    @Query("SELECT a.id FROM ImpactMethodCategory a WHERE a.lifeCycleImpactAssessmentMethod.id = ?1 AND a.status = true")
    List<UUID> findIdByMethod(UUID methodId);

    /**
     * Exists by impact category and life cycle impact assessment method and status method.
     *
     * @param impactCategory                  the impact category
     * @param lifeCycleImpactAssessmentMethod the life cycle impact assessment method
     * @param status                          the status
     * @return the boolean
     */
    boolean existsByImpactCategoryAndLifeCycleImpactAssessmentMethodAndStatus(ImpactCategory impactCategory, LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod, boolean status);

    /**
     * Find method by category method.
     *
     * @param categoryId the category id
     * @return the list
     */
    @Query("SELECT imc FROM ImpactMethodCategory imc " +
            "JOIN FETCH imc.lifeCycleImpactAssessmentMethod m " +
            "WHERE imc.impactCategory.id = :categoryId AND imc.status = true")
    List<ImpactMethodCategory> findMethodByCategory(@Param("categoryId") UUID categoryId);

    /**
     * Find by method and category method.
     *
     * @param categoryId the category id
     * @param methodId   the method id
     * @return the optional
     */
    @Query("SELECT imc FROM ImpactMethodCategory imc " +
            "JOIN FETCH imc.lifeCycleImpactAssessmentMethod m " +
            "JOIN FETCH imc.impactCategory c " +
            "WHERE imc.impactCategory.id = :categoryId AND imc.lifeCycleImpactAssessmentMethod.id = :methodId AND imc.status = true")
    Optional<ImpactMethodCategory> findByMethodAndCategory(@Param("categoryId") UUID categoryId, @Param("methodId") UUID methodId);
}
