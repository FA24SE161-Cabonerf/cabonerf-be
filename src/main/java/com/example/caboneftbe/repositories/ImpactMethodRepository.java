package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.LifeCycleImpactAssessmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpactMethodRepository extends JpaRepository<LifeCycleImpactAssessmentMethod, Long> {
}
