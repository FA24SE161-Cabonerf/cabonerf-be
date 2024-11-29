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

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    ProjectDetailResponseDto dto = new ProjectDetailResponseDto();

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

    Project findByNameAndStatus(String name, boolean status);

    @Query("SELECT p FROM Project p WHERE p.user.id = ?1 AND p.organization.id = ?2 AND p.status = true")
    Page<Project> findAll(UUID userId, UUID organizationId, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.user.id = ?1 AND p.organization.id = ?2 AND p.status = true AND p.lifeCycleImpactAssessmentMethod.id = ?3")
    Page<Project> sortByMethod(UUID userId, UUID organizationId, UUID methodId, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.id = ?1 AND p.status = true")
    Optional<Project> findById(UUID id);

    @Query("SELECT count(p) FROM Project p WHERE p.status = true")
    int findAllByStatus();
}
