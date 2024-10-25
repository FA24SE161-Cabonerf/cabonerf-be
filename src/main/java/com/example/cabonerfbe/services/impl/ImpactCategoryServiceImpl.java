package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.ImpactCategory;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.repositories.ImpactCategoryRepository;
import com.example.cabonerfbe.repositories.LifeCycleImpactAssessmentMethodRepository;
import com.example.cabonerfbe.request.BaseImpactCategoryRequest;
import com.example.cabonerfbe.services.ImpactCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpactCategoryServiceImpl implements ImpactCategoryService {
    @Autowired
    private ImpactCategoryRepository impactCategoryRepository;

    @Autowired
    private LifeCycleImpactAssessmentMethodRepository impactMethodRepository;

    @Autowired
    private ImpactCategoryConverter impactCategoryConverter;

    @Override
    public List<ImpactCategoryDto> getImpactCategoryList() {
        List<ImpactCategory> impactCategories = impactCategoryRepository.findAllByStatus(Constants.STATUS_TRUE);
        if (impactCategories.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND);
        }

        return impactCategoryConverter.fromImpactCategoryListToDtoList(impactCategories);
    }

    @Override
    public ImpactCategoryDto getImpactCategoryById(long id) {
        ImpactCategory impactCategory = impactCategoryRepository.findById(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND));
        return impactCategoryConverter.fromImpactCategoryToImpactCategoryDto(impactCategory);
    }

    @Override
    public ImpactCategoryDto createImpactCategoryForImpactMethod(long methodId, BaseImpactCategoryRequest request) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findByIdAndStatus(methodId, Constants.STATUS_TRUE);

        return null;
    }
}
