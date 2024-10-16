package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.ImpactMethodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpactMethodCategoryRepository extends JpaRepository<ImpactMethodCategory,Long> {
    @Query("SELECT a FROM ImpactMethodCategory a WHERE a.impactCategory.id = ?1 AND a.lifeCycleImpactAssessmentMethod.id = ?2")
    ImpactMethodCategory findByImpactCategoryAndImpactMethod(long impactCategoryId, long impactMethodId);
}
