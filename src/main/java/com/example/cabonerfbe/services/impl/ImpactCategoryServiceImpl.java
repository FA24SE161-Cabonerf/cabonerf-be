package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.converter.LifeCycleImpactAssessmentMethodConverter;
import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.MethodDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.BaseImpactCategoryRequest;
import com.example.cabonerfbe.services.ImpactCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImpactCategoryServiceImpl implements ImpactCategoryService {
    public static final long NO_UPDATE = -1;
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
    @Autowired
    private LifeCycleImpactAssessmentMethodConverter methodConverter;

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

    @Override
    public List<MethodDto> getMethodByImpactCategoryId(UUID categoryId) {

        Optional<ImpactCategory> category = impactCategoryRepository.findByIdAndStatus(categoryId, true);
        if (category.isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, MessageConstants.NO_IMPACT_METHOD_FOUND);
        }
        List<ImpactMethodCategory> list = impactMethodCategoryRepository.findMethodByCategory(categoryId);
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        List<LifeCycleImpactAssessmentMethod> methods = new ArrayList<>();
        for (ImpactMethodCategory x : list) {
            methods.add(x.getLifeCycleImpactAssessmentMethod());
        }

        return methods.stream().map(methodConverter::fromMethodToMethodDto).collect(Collectors.toList());
    }

    @Override
    public List<String> deleteCategoryFromMethod(UUID categoryId, UUID methodId) {
        Optional<ImpactCategory> category = impactCategoryRepository.findByIdAndStatus(categoryId, true);
        if (category.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND);
        }
        Optional<ImpactMethodCategory> imc = impactMethodCategoryRepository.findByMethodAndCategory(categoryId, methodId);
        if (imc.isEmpty()) {
            throw CustomExceptions.notFound("Impact category not exist with method");
        }
        imc.get().setStatus(false);
        impactMethodCategoryRepository.save(imc.get());
        return List.of();
    }

    private ImpactCategoryDto mapRequestToImpactCategory(BaseImpactCategoryRequest request, UUID updateId) {
        MidpointImpactCategory midpointImpactCategory = midpointImpactCategoryRepository.findByIdAndStatus(request.getMidpointImpactCategoryId(), Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CATEGORY_FOUND));
        EmissionCompartment emissionCompartment = new EmissionCompartment();
        ImpactCategory impactCategory = new ImpactCategory();
        if (updateId != null) {
            impactCategory = impactCategoryRepository.findByIdAndStatus(updateId, Constants.STATUS_TRUE).orElseThrow(
                    () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND)
            );
        }
        if(request.getEmissionCompartmentId() == null){
            if(impactCategory.getEmissionCompartment() == null){
                emissionCompartment = null;
            }else{
                request.setEmissionCompartmentId(impactCategory.getEmissionCompartment().getId());
                emissionCompartment = emissionCompartmentRepository.findByIdAndStatus(request.getEmissionCompartmentId(), Constants.STATUS_TRUE)
                        .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_EMISSION_COMPARTMENT_FOUND));
            }
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
