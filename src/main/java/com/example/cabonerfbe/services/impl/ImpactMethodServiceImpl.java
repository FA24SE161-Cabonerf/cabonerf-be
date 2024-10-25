package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.converter.LifeCycleImpactAssessmentMethodConverter;
import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.ImpactCategory;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.models.Perspective;
import com.example.cabonerfbe.repositories.ImpactCategoryRepository;
import com.example.cabonerfbe.repositories.ImpactMethodRepository;
import com.example.cabonerfbe.repositories.PerspectiveRepository;
import com.example.cabonerfbe.request.BaseImpactMethodRequest;
import com.example.cabonerfbe.response.ImpactMethodResponse;
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
    private PerspectiveRepository perspectiveRepository;

    @Autowired
    private LifeCycleImpactAssessmentMethodConverter impactMethodConverter;

    @Autowired
    private ImpactCategoryConverter impactCategoryConverter;

    @Override
    public List<LifeCycleImpactAssessmentMethodDto> getAllImpactMethods() {
        List<LifeCycleImpactAssessmentMethod> impactMethods = impactMethodRepository.findAll();
        if (impactMethods.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND);
        }
        return impactMethodConverter.fromMethodListToMethodDtoList(impactMethods);
    }

    @Override
    public LifeCycleImpactAssessmentMethodDto getImpactMethodById(long id) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findById(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND)
        );
        return impactMethodConverter.fromLifeCycleImpactAssessmentMethodToLifeCycleImpactAssessmentMethodDto(impactMethod);
    }

    @Override
    public List<ImpactCategoryDto> getCategoryByMethodId(long id) {
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
    public ImpactMethodResponse updateImpactMethodById(long methodId, BaseImpactMethodRequest request) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findByIdAndStatus(methodId, Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND + " id: " + methodId));
        mapRequestToImpactMethod(impactMethod, request);

        return impactMethodConverter.fromImpactMethodToImpactMethodResponse(impactMethodRepository.save(impactMethod));
    }

    @Override
    public ImpactMethodResponse deleteImpactMethodById(long methodId) {
        LifeCycleImpactAssessmentMethod impactMethod = impactMethodRepository.findByIdAndStatus(methodId, Constants.STATUS_TRUE)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND + " id: " + methodId));
        impactMethod.setStatus(Constants.STATUS_FALSE);
        return impactMethodConverter.fromImpactMethodToImpactMethodResponse(impactMethodRepository.save(impactMethod));
    }

    private LifeCycleImpactAssessmentMethod mapRequestToImpactMethod(LifeCycleImpactAssessmentMethod impactMethod, BaseImpactMethodRequest request) {
        String methodName = request.getName();
        String description = request.getDescription();
        String version = request.getVersion();
        String reference = request.getReference();
        long perspectiveId = request.getPerspectiveId();

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