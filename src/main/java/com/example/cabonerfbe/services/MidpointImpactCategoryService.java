package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.MidpointImpactCategoryDto;
import com.example.cabonerfbe.response.MidpointImpactCategoryResponse;

import java.util.List;

public interface MidpointImpactCategoryService {
    List<MidpointImpactCategoryResponse> getAllMidpointImpactCategories();

    List<MidpointImpactCategoryDto> get();
    MidpointImpactCategoryDto create();
    MidpointImpactCategoryDto update();
    MidpointImpactCategoryDto delete();
}
