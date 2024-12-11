package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.request.BaseImpactMethodRequest;
import com.example.cabonerfbe.response.GetNameMethodResponse;
import com.example.cabonerfbe.response.ImpactMethodCategoryResponse;
import com.example.cabonerfbe.response.ImpactMethodResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Impact method service.
 *
 * @author SonPHH.
 */
public interface ImpactMethodService {
    /**
     * Gets all impact methods.
     *
     * @return the all impact methods
     */
    List<LifeCycleImpactAssessmentMethodDto> getAllImpactMethods();

    /**
     * Gets impact method by id.
     *
     * @param id the id
     * @return the impact method by id
     */
    LifeCycleImpactAssessmentMethodDto getImpactMethodById(UUID id);

    /**
     * Gets category by method id.
     *
     * @param id the id
     * @return the category by method id
     */
    List<ImpactCategoryDto> getCategoryByMethodId(UUID id);

    /**
     * Create impact method method.
     *
     * @param request the request
     * @return the impact method response
     */
    ImpactMethodResponse createImpactMethod(BaseImpactMethodRequest request);

    /**
     * Update impact method by id method.
     *
     * @param methodId the method id
     * @param request  the request
     * @return the impact method response
     */
    ImpactMethodResponse updateImpactMethodById(UUID methodId, BaseImpactMethodRequest request);

    /**
     * Delete impact method by id method.
     *
     * @param methodId the method id
     * @return the impact method response
     */
    ImpactMethodResponse deleteImpactMethodById(UUID methodId);

    /**
     * Add impact category to impact method method.
     *
     * @param methodId   the method id
     * @param categoryId the category id
     * @return the impact method category response
     */
    ImpactMethodCategoryResponse addImpactCategoryToImpactMethod(UUID methodId, UUID categoryId);

    /**
     * Gets name all method.
     *
     * @return the name all method
     */
    List<GetNameMethodResponse> getNameAllMethod();
}
