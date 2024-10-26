package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.request.BaseImpactCategoryRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface ImpactCategoryService {
    List<ImpactCategoryDto> getImpactCategoryList();
    ImpactCategoryDto getImpactCategoryById(long id);

    ImpactCategoryDto createImpactCategory(BaseImpactCategoryRequest request);

    ImpactCategoryDto updateImpactCategoryById(long categoryId, BaseImpactCategoryRequest request);

    ImpactCategoryDto deleteImpactCategoryById(long categoryId);
}
