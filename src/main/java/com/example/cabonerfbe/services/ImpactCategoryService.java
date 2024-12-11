package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.MethodDto;
import com.example.cabonerfbe.request.BaseImpactCategoryRequest;

import java.util.List;
import java.util.UUID;

/**
 * The interface Impact category service.
 *
 * @author SonPHH.
 */
public interface ImpactCategoryService {
    /**
     * Gets impact category list.
     *
     * @return the impact category list
     */
    List<ImpactCategoryDto> getImpactCategoryList();

    /**
     * Gets impact category by id.
     *
     * @param id the id
     * @return the impact category by id
     */
    ImpactCategoryDto getImpactCategoryById(UUID id);

    /**
     * Create impact category method.
     *
     * @param request the request
     * @return the impact category dto
     */
    ImpactCategoryDto createImpactCategory(BaseImpactCategoryRequest request);

    /**
     * Update impact category by id method.
     *
     * @param categoryId the category id
     * @param request    the request
     * @return the impact category dto
     */
    ImpactCategoryDto updateImpactCategoryById(UUID categoryId, BaseImpactCategoryRequest request);

    /**
     * Delete impact category by id method.
     *
     * @param categoryId the category id
     * @return the impact category dto
     */
    ImpactCategoryDto deleteImpactCategoryById(UUID categoryId);

    /**
     * Gets method by impact category id.
     *
     * @param categoryId the category id
     * @return the method by impact category id
     */
    List<MethodDto> getMethodByImpactCategoryId(UUID categoryId);

    /**
     * Delete category from method method.
     *
     * @param categoryId the category id
     * @param methodId   the method id
     * @return the list
     */
    List<String> deleteCategoryFromMethod(UUID categoryId, UUID methodId);
}
