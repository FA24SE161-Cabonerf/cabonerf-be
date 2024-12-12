package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.dto.ProjectDetailResponseDto;
import com.example.cabonerfbe.models.Project;
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
 * The interface Project repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    /**
     * The constant dto.
     */
    ProjectDetailResponseDto dto = new ProjectDetailResponseDto();

    /**
     * Gets project level detail.
     *
     * @param projectId the project id
     * @return the project level detail
     */
    @Query("SELECT dto(" +
            "p.name, p.modifiedAt, lciam.name, ic.name, mic.name, mic.abbr, piv.value, mic.unit.name) " +
            "FROM Project p " +
            "JOIN ProjectImpactValue piv ON p.id = piv.project.id " +
            "JOIN ImpactMethodCategory imc ON imc.id = piv.impactMethodCategory.id " +
            "JOIN LifeCycleImpactAssessmentMethod lciam ON lciam.id = imc.lifeCycleImpactAssessmentMethod.id " +
            "JOIN ImpactCategory ic ON ic.id = imc.impactCategory.id " +
            "JOIN MidpointImpactCategory mic ON mic.id = ic.midpointImpactCategory.id " +
            "JOIN Unit u ON u.id = mic.unit.id " +
            "WHERE p.id = :projectId")
    List<ProjectDetailResponseDto> getProjectLevelDetail(@Param("projectId") UUID projectId);

    /**
     * Find by name and status method.
     *
     * @param name   the name
     * @param status the status
     * @return the project
     */
    Project findByNameAndStatus(String name, boolean status);

    /**
     * Find all method.
     *
     * @param organizationId the organization id
     * @param pageable       the pageable
     * @return the page
     */
    @Query("SELECT p FROM Project p WHERE p.organization.id = :organizationId AND p.status = true")
    Page<Project> findAll(@Param("organizationId") UUID organizationId, Pageable pageable);

    /**
     * Sort by method method.
     *
     * @param organizationId the organization id
     * @param methodId       the method id
     * @param pageable       the pageable
     * @return the page
     */
    @Query("SELECT p FROM Project p WHERE p.organization.id = :organizationId AND p.status = true AND p.lifeCycleImpactAssessmentMethod.id = :methodId")
    Page<Project> sortByMethod(@Param("organizationId") UUID organizationId, @Param("methodId") UUID methodId, Pageable pageable);

    /**
     * Find by id and status true method.
     *
     * @param id the id
     * @return the optional
     */
    @Query("SELECT p FROM Project p WHERE p.id = ?1 AND p.status = true")
    Optional<Project> findByIdAndStatusTrue(UUID id);

    /**
     * Find all by status method.
     *
     * @return the int
     */
    @Query("SELECT count(p) FROM Project p WHERE p.status = true")
    int findAllByStatus();

    /**
     * Exists by id and status method.
     *
     * @param id     the id
     * @param status the status
     * @return the boolean
     */
    @Query("select (count(p) > 0) from Project p where p.id = ?1 and p.status = ?2")
    boolean existsByIdAndStatus(UUID id, boolean status);
}
