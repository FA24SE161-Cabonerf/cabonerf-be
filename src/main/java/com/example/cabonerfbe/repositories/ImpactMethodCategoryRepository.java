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

@Repository
public interface ImpactMethodCategoryRepository extends JpaRepository<ImpactMethodCategory, UUID> {
    @Query("SELECT a FROM ImpactMethodCategory a " +
            "JOIN FETCH a.impactCategory ic " +
            "JOIN FETCH a.lifeCycleImpactAssessmentMethod lcim " +
            "WHERE ic.id = ?1 AND lcim.id = ?2")
    ImpactMethodCategory findByImpactCategoryAndImpactMethod(UUID impactCategoryId, UUID impactMethodId);


    @Query("SELECT a FROM ImpactMethodCategory a WHERE a.lifeCycleImpactAssessmentMethod.id = ?1")
    List<ImpactMethodCategory> findByMethod(UUID methodId);

    boolean existsByImpactCategoryAndLifeCycleImpactAssessmentMethodAndStatus(ImpactCategory impactCategory, LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod, boolean status);

    @Query("SELECT imc FROM ImpactMethodCategory imc " +
            "JOIN FETCH imc.lifeCycleImpactAssessmentMethod m " +
            "WHERE imc.impactCategory.id = :categoryId AND imc.status = true")
    List<ImpactMethodCategory> findMethodByCategory(@Param("categoryId") UUID categoryId);

    @Query("SELECT imc FROM ImpactMethodCategory imc " +
            "JOIN FETCH imc.lifeCycleImpactAssessmentMethod m " +
            "JOIN FETCH imc.impactCategory c " +
            "WHERE imc.impactCategory.id = :categoryId AND imc.lifeCycleImpactAssessmentMethod.id = :methodId AND imc.status = true")
    Optional<ImpactMethodCategory> findByMethodAndCategory(@Param("categoryId") UUID categoryId, @Param("methodId") UUID methodId);
}
