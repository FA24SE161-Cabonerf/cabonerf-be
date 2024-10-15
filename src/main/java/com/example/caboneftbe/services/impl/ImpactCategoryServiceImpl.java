package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.ImpactCategoryConverter;
import com.example.caboneftbe.dto.ImpactCategoryDto;
import com.example.caboneftbe.enums.MessageConstants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.repositories.ImpactCategoryRepository;
import com.example.caboneftbe.response.GetImpactCategoryListResponse;
import com.example.caboneftbe.services.ImpactCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImpactCategoryServiceImpl implements ImpactCategoryService {
    @Autowired
    private ImpactCategoryRepository impactCategoryRepository;

    @Autowired
    private ImpactCategoryConverter impactCategoryConverter;

    @Override
    public List<ImpactCategoryDto> getImpactCategoryList() {
        List<ImpactCategory> impactCategories = impactCategoryRepository.findAllByStatus(true);
        if (impactCategories.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND);
        }
        List<ImpactCategoryDto> impactCategoryDtos = impactCategoryConverter.fromImpactCategoryListToDtoList(impactCategories);

        return impactCategoryDtos;
    }

    @Override
    public ImpactCategoryDto getImpactCategoryById(long id) {
        ImpactCategory impactCategory = impactCategoryRepository.findById(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND));
        return impactCategoryConverter.fromImpactCategoryToImpactCategoryDto(impactCategory);
    }
}
