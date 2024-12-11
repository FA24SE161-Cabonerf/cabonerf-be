package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.ProjectImpactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The interface Project impact value repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ProjectImpactValueRepository extends JpaRepository<ProjectImpactValue, UUID> {

    /**
     * Find all by project id method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("SELECT piv FROM ProjectImpactValue piv " +
            "JOIN FETCH piv.impactMethodCategory imc " +
            "JOIN FETCH imc.lifeCycleImpactAssessmentMethod lcim " +
            "WHERE piv.project.id = :projectId " +
            "ORDER BY imc.id ASC")
    List<ProjectImpactValue> findAllByProjectId(@Param("projectId") UUID projectId);

    /**
     * Find by project id method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("SELECT piv FROM ProjectImpactValue piv WHERE piv.project.id = ?1 AND piv.status = true")
    List<ProjectImpactValue> findByProjectId(UUID projectId);

    /**
     * Gets value.
     *
     * @param projectId the project id
     * @return the value
     */
    @Query("SELECT piv FROM ProjectImpactValue piv WHERE piv.project.id = ?1 AND piv.value > 0")
    List<ProjectImpactValue> getValue(UUID projectId);

    /**
     * Gets sum impact.
     *
     * @param categoryId the category id
     * @return the sum impact
     */
    @Query("SELECT SUM(p.value) FROM ProjectImpactValue p WHERE p.impactMethodCategory.impactCategory.id = :categoryId")
    BigDecimal getSumImpact(@Param("categoryId") UUID categoryId);
}
