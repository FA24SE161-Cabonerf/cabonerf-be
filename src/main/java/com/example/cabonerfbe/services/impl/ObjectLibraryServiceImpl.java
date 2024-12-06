package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.dto.ObjectLibraryDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.SearchObjectLibraryDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.models.UserOrganization;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.AddObjectLibraryRequest;
import com.example.cabonerfbe.request.PagingKeywordMethodRequest;
import com.example.cabonerfbe.response.DeleteProcessResponse;
import com.example.cabonerfbe.services.MessagePublisher;
import com.example.cabonerfbe.services.ObjectLibraryService;
import com.example.cabonerfbe.services.ProcessImpactValueService;
import com.example.cabonerfbe.services.ProcessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ObjectLibraryServiceImpl implements ObjectLibraryService {

    private final OrganizationRepository organizationRepository;
    private final ProcessRepository processRepository;
    private final LifeCycleImpactAssessmentMethodRepository methodRepository;
    private final ProcessConverter processConverter;
    private final ProcessService processService;
    private final ProcessImpactValueRepository processImpactValueRepository;
    private final ExchangesConverter exchangesConverter;
    private final ExchangesRepository exchangesRepository;
    private final UserOrganizationRepository userOrganizationRepository;
    private final ProjectRepository projectRepository;
    private final MessagePublisher messagePublisher;
    private final ProcessImpactValueService processImpactValueService;
    private final ProjectImpactValueRepository projectImpactValueRepository;

    public ObjectLibraryServiceImpl(ProcessService processService, OrganizationRepository organizationRepository, ProcessRepository processRepository, LifeCycleImpactAssessmentMethodRepository methodRepository, ProcessConverter processConverter, ProcessImpactValueRepository processImpactValueRepository, ExchangesConverter exchangesConverter, ExchangesRepository exchangesRepository, ExchangesConverter exchangesConverter1, ExchangesRepository exchangesRepository1, UserOrganizationRepository userOrganizationRepository, LifeCycleStageRepository lifeCycleStageRepository, ProjectRepository projectRepository, MessagePublisher messagePublisher, ProcessImpactValueService processImpactValueService, ProjectImpactValueRepository projectImpactValueRepository) {
        this.organizationRepository = organizationRepository;
        this.processRepository = processRepository;
        this.methodRepository = methodRepository;
        this.processConverter = processConverter;
        this.processService = processService;
        this.processImpactValueRepository = processImpactValueRepository;
        this.exchangesConverter = exchangesConverter1;
        this.exchangesRepository = exchangesRepository1;
        this.userOrganizationRepository = userOrganizationRepository;
        this.projectRepository = projectRepository;
        this.messagePublisher = messagePublisher;
        this.processImpactValueService = processImpactValueService;
        this.projectImpactValueRepository = projectImpactValueRepository;
    }

    @Transactional
    @Override
    public SearchObjectLibraryDto searchObjectLibraryOfOrganization(UUID userId, UUID organizationId, PagingKeywordMethodRequest request) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_ORGANIZATION_FOUND)
        );

        UserOrganization userOrganization = userOrganizationRepository.findByUserAndOrganization(organizationId, userId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_BELONG_TO_ORGANIZATION));

        validateMethod(request.getMethodId());

        Pageable pageable = PageRequest.of(request.getCurrentPage() - 1, request.getPageSize());

        Page<Process> processPage = processRepository.findObjectLibrary(organizationId, request.getMethodId(), request.getKeyword(), pageable);

        List<ObjectLibraryDto> objectLibraryList = processPage.getContent().stream().map(
                process -> {
                    ObjectLibraryDto object = processConverter.fromProcessToObjectLibraryDto(process);
                    object.setImpacts(processService.converterProcess(
                            processImpactValueRepository.findByProcessId(process.getId())));
                    object.setExchanges(exchangesConverter.fromExchangesToExchangesDto(
                            exchangesRepository.findAllByProcessJoinFetch(process.getId())));
                    return object;
                }
        ).toList();

        int totalPage = processPage.getTotalPages();

        boolean exceeded = request.getCurrentPage() > totalPage;

        return SearchObjectLibraryDto.builder()
                .totalPage(exceeded ? 0 : totalPage)
                .pageSize(request.getPageSize())
                .pageCurrent(exceeded ? 1 : request.getCurrentPage())
                .objectLibraryList(exceeded ? Collections.emptyList() : objectLibraryList)
                .build();

    }

    @Override
    public DeleteProcessResponse removeFromObjectLibrary(UUID userId, UUID pid) {
        Process process = processRepository.findByProcessIdAndLibrary(pid, Constants.IS_LIB_TRUE).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.PROCESS_NOT_FOUND_IN_OBJECT_LIBRARY)
        );

        UserOrganization userOrganization = userOrganizationRepository.findByUserAndOrganization(process.getOrganization().getId(), userId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_BELONG_TO_ORGANIZATION));

        process.setStatus(false);
        processRepository.save(process);
        return new DeleteProcessResponse(process.getId());
    }

    @Transactional
    @Override
    public List<Process> saveToObjectLibrary(UUID userId, UUID projectId) {
        Project project = projectRepository.findByIdAndStatusTrue(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND)
        );

        if (projectImpactValueRepository.findAllByProjectId(projectId).isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.CALCULATION_REQUIRED_TO_SAVE_OBJECT_LIBRARY);
        }

        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_IN_PROJECT, Collections.EMPTY_LIST);
        }

        Process saveProcess = processList.get(0);

        if (processList.size() > 1) {
            saveProcess = processRepository.findRootProcess(projectId).get(0);
        }

        UserOrganization userOrganization = userOrganizationRepository.findByUserAndOrganization(saveProcess.getProject().getOrganization().getId(), userId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_BELONG_TO_ORGANIZATION));

        processService.convertProcessToObjectLibrary(saveProcess);

        return new ArrayList<>();
    }

    @Override
    public ProcessDto addFromObjectLibraryToProject(AddObjectLibraryRequest request) {
        UUID userId = request.getUserId();
        UUID projectId = request.getProjectId();
        UUID objectLibId = request.getObjectLibId();

        Project project = projectRepository.findByIdAndStatusTrue(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND)
        );

        UserOrganization userOrganization = userOrganizationRepository.findByUserAndOrganization(
                project.getOrganization().getId(), userId).orElseThrow(() ->
                CustomExceptions.unauthorized(MessageConstants.USER_NOT_BELONG_TO_ORGANIZATION)
        );

        // limit 20 processes in 1 project
        if (processRepository.countAllByProject_Id(projectId)) {
            throw CustomExceptions.badRequest(MessageConstants.MAX_PROCESS_EXCEEDED);
        }

        Process object = processRepository.findByProcessIdAndLibrary(objectLibId, Constants.IS_LIB_TRUE).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.PROCESS_NOT_FOUND_IN_OBJECT_LIBRARY)
        );

        if (!object.getOrganization().getId().equals(project.getOrganization().getId())) {
            throw CustomExceptions.badRequest(MessageConstants.OBJECT_AND_PROJECT_ORGANIZATION_NOT_SIMILAR);
        }

        UUID projectMethodId = project.getLifeCycleImpactAssessmentMethod().getId();

        if (!object.getMethodId().equals(projectMethodId)) {
            throw CustomExceptions.badRequest(MessageConstants.OBJECT_AND_PROJECT_METHOD_NOT_SIMILAR);
        }

        // duplicate from object lib to a new processDto
        return processService.convertObjectLibraryToProcessDto(object, project);
    }

    private void validateMethod(UUID methodId) {
        methodRepository.findByIdAndStatus(methodId, true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND, Collections.EMPTY_LIST));
    }
}
