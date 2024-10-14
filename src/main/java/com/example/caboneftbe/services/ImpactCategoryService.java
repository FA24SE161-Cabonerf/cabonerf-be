package com.example.caboneftbe.services;

import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.response.GetImpactCategoryListResponse;

import java.util.List;
import java.util.Optional;

public interface ImpactCategoryService {
    GetImpactCategoryListResponse getImpactCategoryList();
    Optional<ImpactCategory> getImpactCategoryById(long id);
}
