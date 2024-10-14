package com.example.caboneftbe.repositories;

import com.example.caboneftbe.dto.ProjectDetailResponseDto;
import com.example.caboneftbe.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
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
    List<ProjectDetailResponseDto> getProjectLevelDetail(@Param("projectId") Long projectId);
}
