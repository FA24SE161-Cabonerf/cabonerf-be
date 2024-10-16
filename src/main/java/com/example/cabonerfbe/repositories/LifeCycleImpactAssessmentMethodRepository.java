package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeCycleImpactAssessmentMethodRepository extends JpaRepository<LifeCycleImpactAssessmentMethod,Long> {

    @Query("SELECT a FROM LifeCycleImpactAssessmentMethod a WHERE a.perspective.name like ?1 and a.name like ?2 ")
    LifeCycleImpactAssessmentMethod findByName(String name, String lifeCycleImpactAssessmentMethodName);

    @Query("SELECT a FROM LifeCycleImpactAssessmentMethod a WHERE a.name like ?1 ")
    List<LifeCycleImpactAssessmentMethod> findAllByName(String name);
}
