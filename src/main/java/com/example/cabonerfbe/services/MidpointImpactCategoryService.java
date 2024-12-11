package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.MidpointImpactCategoryDto;
import com.example.cabonerfbe.request.CreateMidpointImpactCategoryRequest;
import com.example.cabonerfbe.request.UpdateMidpointImpactCategoryRequest;
import com.example.cabonerfbe.response.MidpointImpactCategoryResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Midpoint impact category service.
 *
 * @author SonPHH.
 */
public interface MidpointImpactCategoryService {
    /**
     * Gets all midpoint impact categories.
     *
     * @return the all midpoint impact categories
     */
    List<MidpointImpactCategoryResponse> getAllMidpointImpactCategories();

    /**
     * Get method.
     *
     * @return the list
     */
    List<MidpointImpactCategoryDto> get();

    /**
     * Create method.
     *
     * @param request the request
     * @return the midpoint impact category dto
     */
    MidpointImpactCategoryDto create(CreateMidpointImpactCategoryRequest request);

    /**
     * Update method.
     *
     * @param id      the id
     * @param request the request
     * @return the midpoint impact category dto
     */
    MidpointImpactCategoryDto update(UUID id, UpdateMidpointImpactCategoryRequest request);

    /**
     * Delete method.
     *
     * @param id the id
     * @return the midpoint impact category dto
     */
    MidpointImpactCategoryDto delete(UUID id);
}
