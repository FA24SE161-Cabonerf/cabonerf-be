package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.ImpactCategoryConverter;
import com.example.caboneftbe.converter.LifeCycleImpactAssessmentMethodConverter;
import com.example.caboneftbe.dto.ImpactCategoryDto;
import com.example.caboneftbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.caboneftbe.enums.MessageConstants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.models.LifeCycleImpactAssessmentMethod;
import com.example.caboneftbe.repositories.ImpactCategoryRepository;
import com.example.caboneftbe.repositories.ImpactMethodRepository;
import com.example.caboneftbe.services.ImpactMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
