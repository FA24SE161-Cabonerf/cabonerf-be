package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.ProjectImpactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectImpactValueRepository extends JpaRepository<ProjectImpactValue, Long> {
}
