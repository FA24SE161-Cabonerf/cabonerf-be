package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.MidpointImpactCategoryDto;
import com.example.cabonerfbe.request.CreateMidpointImpactCategoryRequest;
import com.example.cabonerfbe.request.UpdateMidpointImpactCategoryRequest;
import com.example.cabonerfbe.response.MidpointImpactCategoryResponse;

import java.util.List;
import java.util.UUID;

public interface MidpointImpactCategoryService {
    List<MidpointImpactCategoryResponse> getAllMidpointImpactCategories();

    List<MidpointImpactCategoryDto> get();

    MidpointImpactCategoryDto create(CreateMidpointImpactCategoryRequest request);

    MidpointImpactCategoryDto update(UUID id, UpdateMidpointImpactCategoryRequest request);

    MidpointImpactCategoryDto delete(UUID id);
}
