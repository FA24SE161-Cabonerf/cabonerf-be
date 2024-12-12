package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.converter.LifeCycleImpactAssessmentMethodConverter;
import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.ImpactCategory;
import com.example.cabonerfbe.models.ImpactMethodCategory;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.models.Perspective;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.BaseImpactMethodRequest;
import com.example.cabonerfbe.response.GetNameMethodResponse;
import com.example.cabonerfbe.response.ImpactMethodCategoryResponse;
import com.example.cabonerfbe.response.ImpactMethodResponse;
import com.example.cabonerfbe.services.ImpactMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The class Impact method service.
 *
 * @author SonPHH.
 */
@Service
public class ImpactMethodServiceImpl implements ImpactMethodService {
    @Autowired
    private ImpactMethodRepository impactMethodRepository;

    @Autowired
    private ImpactCategoryRepository impactCategoryRepository;

    @Autowired
    private PerspectiveRepository perspectiveRepository;

    @Autowired
    private ImpactMethodCategoryRepository impactMethodCategoryRepository;

    @Autowired
    private LifeCycleImpactAssessmentMethodConverter impactMethodConverter;

    @Autowired
    private ImpactCategoryConverter impactCategoryConverter;

    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;

    @Override
    public List<LifeCycleImpactAssessmentMethodDto> getAllImpactMethods() {
        List<LifeCycleImpactAssessmentMethod> impactMethods = impactMethodRepository.findByStatus(Constants.STATUS_TRUE);
        return impactMethodConverter.fromMethodListToMethodDtoList(impactMethods);
    }

    @Override
    public LifeCycleImpactAssessmentMethodDto getImpactMethodById(UUID id) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findByIdAndStatus(id, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND)
        );
        return impactMethodConverter.fromLifeCycleImpactAssessmentMethodToLifeCycleImpactAssessmentMethodDto(impactMethod);
    }

    @Override
    public List<ImpactCategoryDto> getCategoryByMethodId(UUID id) {
        List<ImpactCategory> impactCategories = impactCategoryRepository.findAllByImpactMethodId(id);
        if (impactCategories.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND);
        }
        return impactCategoryConverter.fromImpactCategoryListToDtoList(impactCategories);
    }

    @Override
    public ImpactMethodResponse createImpactMethod(BaseImpactMethodRequest request) {
        LifeCycleImpactAssessmentMethod impactMethod = mapRequestToImpactMethod(new LifeCycleImpactAssessmentMethod(), request);

        return impactMethodConverter.fromImpactMethodToImpactMethodResponse(impactMethodRepository.save(impactMethod));
    }

    @Override
    public ImpactMethodResponse updateImpactMethodById(UUID methodId, BaseImpactMethodRequest request) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findByIdAndStatus(methodId, Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND + " id: " + methodId));
        mapRequestToImpactMethod(impactMethod, request);

        return impactMethodConverter.fromImpactMethodToImpactMethodResponse(impactMethodRepository.save(impactMethod));
    }

    @Override
    public ImpactMethodResponse deleteImpactMethodById(UUID methodId) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findByIdAndStatus(methodId, Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND + " id: " + methodId));
        impactMethod.setStatus(Constants.STATUS_FALSE);
        return impactMethodConverter.fromImpactMethodToImpactMethodResponse(impactMethodRepository.save(impactMethod));
    }

    @Override
    public ImpactMethodCategoryResponse addImpactCategoryToImpactMethod(UUID methodId, UUID categoryId) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findByIdAndStatus(methodId, Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND + " id: " + methodId));
        ImpactCategory impactCategory = impactCategoryRepository.findByIdAndStatus(categoryId, Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND + " id: " + categoryId));
        if (impactMethodCategoryRepository.existsByImpactCategoryAndLifeCycleImpactAssessmentMethodAndStatus(impactCategory, impactMethod, true)) {
            throw CustomExceptions.badRequest(MessageConstants.IMPACT_CATEGORY_ALREADY_IN_METHOD);
        }
        impactMethodCategoryRepository.save(new ImpactMethodCategory(impactMethod, impactCategory));
        ImpactMethodCategoryResponse response = new ImpactMethodCategoryResponse();
        response.setImpactMethod(impactMethodConverter.fromImpactMethodToImpactMethodResponse(impactMethod));
        response.setImpactCategory(impactCategoryConverter.fromImpactCategoryToImpactCategoryDto(impactCategory));
        return response;
    }

    @Override
    public List<GetNameMethodResponse> getNameAllMethod() {
        List<String> list = methodRepository.getAllWithName();
        return list.stream().map(impactMethodConverter::fromModelToName).collect(Collectors.toList());
    }

    private LifeCycleImpactAssessmentMethod mapRequestToImpactMethod(LifeCycleImpactAssessmentMethod impactMethod, BaseImpactMethodRequest request) {
        String methodName = request.getName();
        String description = request.getDescription();
        String version = request.getVersion();
        String reference = request.getReference();
        UUID perspectiveId = request.getPerspectiveId();

        if (impactMethodRepository.existsByNameIgnoreCaseAndVersionIgnoreCaseAndPerspectiveId(methodName, version, perspectiveId)) {
            throw CustomExceptions.badRequest(MessageConstants.IMPACT_METHOD_EXIST);
        }

        Perspective perspective = perspectiveRepository.findById(perspectiveId).orElseThrow(() ->
                CustomExceptions.notFound(MessageConstants.NO_PERSPECTIVE_FOUND)
        );

        impactMethod.setName(methodName);
        impactMethod.setDescription(description);
        impactMethod.setVersion(version);
        impactMethod.setReference(reference);
        impactMethod.setPerspective(perspective);

        return impactMethod;

    }
}