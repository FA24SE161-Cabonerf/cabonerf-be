package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpactMethodRepository extends JpaRepository<LifeCycleImpactAssessmentMethod, Long> {
}
