package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ImpactCategoryDto;

import java.util.List;

public interface ImpactCategoryService {
    List<ImpactCategoryDto> getImpactCategoryList();
    ImpactCategoryDto getImpactCategoryById(long id);
}
