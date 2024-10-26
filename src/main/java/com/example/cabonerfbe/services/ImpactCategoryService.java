package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.request.BaseImpactCategoryRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ImpactCategoryService {
    List<ImpactCategoryDto> getImpactCategoryList();
    ImpactCategoryDto getImpactCategoryById(UUID id);

    ImpactCategoryDto createImpactCategory(BaseImpactCategoryRequest request);

    ImpactCategoryDto updateImpactCategoryById(UUID categoryId, BaseImpactCategoryRequest request);

    ImpactCategoryDto deleteImpactCategoryById(UUID categoryId);
}
