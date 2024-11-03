package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.BaseImpactCategoryRequest;
import com.example.cabonerfbe.services.ImpactCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ImpactCategoryServiceImpl implements ImpactCategoryService {
    @Autowired
    private ImpactCategoryRepository impactCategoryRepository;
    @Autowired
    private MidpointImpactCategoryRepository midpointImpactCategoryRepository;
    @Autowired
    private EmissionCompartmentRepository emissionCompartmentRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository impactMethodRepository;
    @Autowired
    private ImpactMethodCategoryRepository impactMethodCategoryRepository;
    @Autowired
    private ImpactCategoryConverter impactCategoryConverter;

    public static final long NO_UPDATE = -1;


    @Override
    public List<ImpactCategoryDto> getImpactCategoryList() {
        List<ImpactCategory> impactCategories = impactCategoryRepository.findAllByStatus(Constants.STATUS_TRUE);
        return impactCategoryConverter.fromImpactCategoryListToDtoList(impactCategories);
    }

    @Override
    public ImpactCategoryDto getImpactCategoryById(UUID id) {
        ImpactCategory impactCategory = impactCategoryRepository.findByIdAndStatus(id, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND));
        return impactCategoryConverter.fromImpactCategoryToImpactCategoryDto(impactCategory);
    }

    @Override
    public ImpactCategoryDto createImpactCategory(BaseImpactCategoryRequest request) {
        return mapRequestToImpactCategory(request, null);
    }

    @Override
    public ImpactCategoryDto updateImpactCategoryById(UUID categoryId, BaseImpactCategoryRequest request) {
        return mapRequestToImpactCategory(request, categoryId);
    }

    @Override
    public ImpactCategoryDto deleteImpactCategoryById(UUID categoryId) {
        ImpactCategory impactCategory = impactCategoryRepository.findByIdAndStatus(categoryId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND)
        );
        impactCategory.setStatus(Constants.STATUS_FALSE);
        return impactCategoryConverter.fromImpactCategoryToImpactCategoryDto(impactCategoryRepository.save(impactCategory));
    }

    private ImpactCategoryDto mapRequestToImpactCategory(BaseImpactCategoryRequest request, UUID updateId) {
        MidpointImpactCategory midpointImpactCategory = midpointImpactCategoryRepository.findByIdAndStatus(request.getMidpointImpactCategoryId(), Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CATEGORY_FOUND));
        EmissionCompartment emissionCompartment = emissionCompartmentRepository.findByIdAndStatus(request.getEmissionCompartmentId(), Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_EMISSION_COMPARTMENT_FOUND));
        ImpactCategory impactCategory = new ImpactCategory();
        if (updateId != null) {
            impactCategory = impactCategoryRepository.findByIdAndStatus(updateId, Constants.STATUS_TRUE).orElseThrow(
                    () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND)
            );
        }
        impactCategory.setName(request.getName());
        impactCategory.setIndicator(request.getIndicator());
        impactCategory.setIndicatorDescription(request.getIndicatorDescription());
        impactCategory.setUnit(request.getUnit());
        impactCategory.setEmissionCompartment(emissionCompartment);
        impactCategory.setMidpointImpactCategory(midpointImpactCategory);
        impactCategoryRepository.save(impactCategory);
        return impactCategoryConverter.fromImpactCategoryToImpactCategoryDto(impactCategory);
    }
}
