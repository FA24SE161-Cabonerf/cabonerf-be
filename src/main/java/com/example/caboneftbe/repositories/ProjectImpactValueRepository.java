package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.ProjectImpactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectImpactValueRepository extends JpaRepository<ProjectImpactValue, Long> {
}
