package com.example.caboneftbe.services;

import com.example.caboneftbe.dto.ImpactCategoryDto;
import com.example.caboneftbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.models.LifeCycleImpactAssessmentMethod;

import java.util.List;
import java.util.Optional;

public interface ImpactMethodService {
    List<LifeCycleImpactAssessmentMethodDto> getAllImpactMethods();
    LifeCycleImpactAssessmentMethodDto getImpactMethodById(long id);

    List<ImpactCategoryDto> getCategoryByMethodId(long id);
}
