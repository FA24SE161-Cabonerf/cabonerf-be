package com.example.caboneftbe.services;

import com.example.caboneftbe.dto.ImpactCategoryDto;
import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.response.GetImpactCategoryListResponse;

import java.util.List;
import java.util.Optional;

public interface ImpactCategoryService {
    List<ImpactCategoryDto> getImpactCategoryList();
    ImpactCategoryDto getImpactCategoryById(long id);
}
