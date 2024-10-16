package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;

import java.util.List;

public interface ImpactMethodService {
    List<LifeCycleImpactAssessmentMethodDto> getAllImpactMethods();
    LifeCycleImpactAssessmentMethodDto getImpactMethodById(long id);

    List<ImpactCategoryDto> getCategoryByMethodId(long id);
}
