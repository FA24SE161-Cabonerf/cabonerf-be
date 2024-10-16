package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.converter.LifeCycleImpactAssessmentMethodConverter;
import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.ImpactCategory;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.repositories.ImpactCategoryRepository;
import com.example.cabonerfbe.repositories.ImpactMethodRepository;
import com.example.cabonerfbe.services.ImpactMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpactMethodServiceImpl implements ImpactMethodService {
    @Autowired
    private ImpactMethodRepository impactMethodRepository;

    @Autowired
    private ImpactCategoryRepository impactCategoryRepository;

    @Autowired
    private LifeCycleImpactAssessmentMethodConverter lifeCycleImpactAssessmentMethodConverter;

    @Autowired
    private ImpactCategoryConverter impactCategoryConverter;

    @Override
    public List<LifeCycleImpactAssessmentMethodDto> getAllImpactMethods() {
        List<LifeCycleImpactAssessmentMethod> impactMethods = impactMethodRepository.findAll();
        if (impactMethods.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND);
        }
        return lifeCycleImpactAssessmentMethodConverter.fromMethodListToMethodDtoList(impactMethods);
    }

    @Override
    public LifeCycleImpactAssessmentMethodDto getImpactMethodById(long id) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findById(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND)
        );
        return lifeCycleImpactAssessmentMethodConverter.fromLifeCycleImpactAssessmentMethodToLifeCycleImpactAssessmentMethodDto(impactMethod);
    }

    @Override
    public List<ImpactCategoryDto> getCategoryByMethodId(long id) {
        List<ImpactCategory> impactCategories = impactCategoryRepository.findAllByImpactMethodId(id);
        if (impactCategories.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND);
        };
        return impactCategoryConverter.fromImpactCategoryListToDtoList(impactCategories);
    }
}
