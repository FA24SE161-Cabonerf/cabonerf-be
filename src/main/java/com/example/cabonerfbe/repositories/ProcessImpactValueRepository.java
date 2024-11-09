package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.ProcessImpactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessImpactValueRepository extends JpaRepository<ProcessImpactValue, UUID> {
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 ORDER BY p.impactMethodCategory.impactCategory.name asc")
    List<ProcessImpactValue> findByProcessId(UUID id);

    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 AND p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = ?2")
    List<ProcessImpactValue> findByProcessAndMethod(UUID id, UUID methodId);

    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 AND p.impactMethodCategory.id = ?2 AND p.status = true")
    Optional<ProcessImpactValue> findByProcessIdAndImpactMethodCategoryId(UUID processId, UUID methodCategoryId);
}
