package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.enums.MessageConstants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.models.LifeCycleImpactAssessmentMethod;
import com.example.caboneftbe.repositories.ImpactCategoryRepository;
import com.example.caboneftbe.repositories.ImpactMethodRepository;
import com.example.caboneftbe.services.ImpactMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImpactMethodServiceImpl implements ImpactMethodService {
    @Autowired
    private ImpactMethodRepository impactMethodRepository;

    @Autowired
    private ImpactCategoryRepository impactCategoryRepository;

    @Override
    public List<LifeCycleImpactAssessmentMethod> getAllImpactMethods() {
        List<LifeCycleImpactAssessmentMethod> impactMethods = impactMethodRepository.findAll();
        if (impactMethods.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND);
        }
        return impactMethods;
    }

    @Override
    public Optional<LifeCycleImpactAssessmentMethod> getImpactMethodById(long id) {
        Optional<LifeCycleImpactAssessmentMethod> impactMethod = impactMethodRepository.findById(id);
        if (impactMethod.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND);
        }
        return impactMethod;
    }

    @Override
    public List<ImpactCategory> getCategoryByMethodId(long id) {
        List<ImpactCategory> impactCategories = impactCategoryRepository.findAllByImpactMethodId(id);
        if (impactCategories.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND);
        };
        return impactCategories;
    }
}
