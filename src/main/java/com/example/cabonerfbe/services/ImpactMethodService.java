package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.request.BaseImpactMethodRequest;
import com.example.cabonerfbe.response.ImpactMethodCategoryResponse;
import com.example.cabonerfbe.response.ImpactMethodResponse;

import java.util.List;
import java.util.UUID;

public interface ImpactMethodService {
    List<LifeCycleImpactAssessmentMethodDto> getAllImpactMethods();
    LifeCycleImpactAssessmentMethodDto getImpactMethodById(UUID id);

    List<ImpactCategoryDto> getCategoryByMethodId(UUID id);

    ImpactMethodResponse createImpactMethod(BaseImpactMethodRequest request);

    ImpactMethodResponse updateImpactMethodById(UUID methodId, BaseImpactMethodRequest request);

    ImpactMethodResponse deleteImpactMethodById(UUID methodId);

    ImpactMethodCategoryResponse addImpactCategoryToImpactMethod(UUID methodId, UUID categoryId);
}
