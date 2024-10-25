package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.request.BaseImpactMethodRequest;
import com.example.cabonerfbe.response.ImpactMethodCategoryResponse;
import com.example.cabonerfbe.response.ImpactMethodResponse;

import java.util.List;

public interface ImpactMethodService {
    List<LifeCycleImpactAssessmentMethodDto> getAllImpactMethods();
    LifeCycleImpactAssessmentMethodDto getImpactMethodById(long id);

    List<ImpactCategoryDto> getCategoryByMethodId(long id);

    ImpactMethodResponse createImpactMethod(BaseImpactMethodRequest request);

    ImpactMethodResponse updateImpactMethodById(long methodId, BaseImpactMethodRequest request);

    ImpactMethodResponse deleteImpactMethodById(long methodId);

    ImpactMethodCategoryResponse addImpactCategoryToImpactMethod(long methodId, long categoryId);
}
