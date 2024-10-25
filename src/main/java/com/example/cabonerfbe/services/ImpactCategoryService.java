package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.request.BaseImpactCategoryRequest;

import java.util.List;

public interface ImpactCategoryService {
    List<ImpactCategoryDto> getImpactCategoryList();
    ImpactCategoryDto getImpactCategoryById(long id);

    ImpactCategoryDto createImpactCategoryForImpactMethod(long methodId, BaseImpactCategoryRequest request);
}
