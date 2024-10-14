package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.models.ImpactMethodCategory;
import com.example.caboneftbe.models.LifeCycleImpactAssessmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpactMethodCategoryRepository extends JpaRepository<ImpactMethodCategory,Long> {
    @Query("SELECT a FROM ImpactMethodCategory a WHERE a.impactCategory.id = ?1 AND a.lifeCycleImpactAssessmentMethod.id = ?2")
    ImpactMethodCategory findByImpactCategoryAndImpactMethod(long impactCategoryId, long impactMethodId);
}
