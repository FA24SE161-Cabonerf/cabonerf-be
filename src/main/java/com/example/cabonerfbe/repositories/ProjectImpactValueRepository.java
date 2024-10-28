package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.models.ProjectImpactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectImpactValueRepository extends JpaRepository<ProjectImpactValue, UUID> {

    @Query("SELECT p FROM ProjectImpactValue p WHERE p.project.id = ?1 ORDER BY p.impactMethodCategory.impactCategory.id ASC")
    List<ProjectImpactValue> findAllByProjectId(UUID id);

}
