package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.models.ProjectImpactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectImpactValueRepository extends JpaRepository<ProjectImpactValue, UUID> {

    @Query("SELECT piv FROM ProjectImpactValue piv " +
            "JOIN FETCH piv.impactMethodCategory imc " +
            "JOIN FETCH imc.lifeCycleImpactAssessmentMethod lcim " +
            "WHERE piv.project.id = :projectId " +
            "ORDER BY imc.id ASC")
    List<ProjectImpactValue> findAllByProjectId(@Param("projectId") UUID projectId);

    @Query("SELECT piv FROM ProjectImpactValue piv WHERE piv.project.id = ?1 AND piv.status = true")
    List<ProjectImpactValue> findByProjectId(UUID projectId);
}
