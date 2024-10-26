package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.models.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ImpactMethodRepository extends JpaRepository<LifeCycleImpactAssessmentMethod, UUID> {

    @Query("""
            select (count(l) > 0) from LifeCycleImpactAssessmentMethod l
            where upper(l.name) = upper(?1) and upper(l.version) = upper(?2) and l.perspective.id = ?3 and l.status = true
           """)
    boolean existsByNameIgnoreCaseAndVersionIgnoreCaseAndPerspectiveId(String name, String version, UUID perspectiveId);

    Optional<LifeCycleImpactAssessmentMethod> findByIdAndStatus(UUID methodId, boolean status);
}
