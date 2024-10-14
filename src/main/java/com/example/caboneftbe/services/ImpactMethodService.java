package com.example.caboneftbe.services;

import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.models.LifeCycleImpactAssessmentMethod;

import java.util.List;
import java.util.Optional;

public interface ImpactMethodService {
    List<LifeCycleImpactAssessmentMethod> getAllImpactMethods();
    Optional<LifeCycleImpactAssessmentMethod> getImpactMethodById(long id);

    List<ImpactCategory> getCategoryByMethodId(long id);
}
