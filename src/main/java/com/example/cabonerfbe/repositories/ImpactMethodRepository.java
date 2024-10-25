package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.models.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImpactMethodRepository extends JpaRepository<LifeCycleImpactAssessmentMethod, Long> {

    @Query("""
            select (count(l) > 0) from LifeCycleImpactAssessmentMethod l
            where upper(l.name) = upper(?1) and upper(l.version) = upper(?2) and l.perspective.id = ?3 and l.status = true
           """)
    boolean existsByNameIgnoreCaseAndVersionIgnoreCaseAndPerspectiveId(String name, String version, Long perspectiveId);

    Optional<LifeCycleImpactAssessmentMethod> findByIdAndStatus(long methodId, boolean status);
}
