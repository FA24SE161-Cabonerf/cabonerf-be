package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LifeCycleImpactAssessmentMethodRepository extends JpaRepository<LifeCycleImpactAssessmentMethod, UUID> {

    @Query("SELECT a FROM LifeCycleImpactAssessmentMethod a " +
            "JOIN FETCH a.perspective p " +
            "WHERE p.name LIKE ?1 AND a.name LIKE ?2")
    LifeCycleImpactAssessmentMethod findByName(String perspectiveName, String lifeCycleImpactAssessmentMethodName);


    @Query("SELECT a FROM LifeCycleImpactAssessmentMethod a WHERE a.name like ?1 ")
    List<LifeCycleImpactAssessmentMethod> findAllByName(String name);

    Optional<LifeCycleImpactAssessmentMethod> findByIdAndStatus(UUID id, boolean status);

    @Query("SELECT m.name FROM LifeCycleImpactAssessmentMethod m GROUP BY m.name")
    List<String> getAllWithName();


}
