package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.models.ProjectImpactValue;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.request.UpdateProjectDetailRequest;
import com.example.cabonerfbe.response.CreateProjectResponse;
import com.example.cabonerfbe.response.GetAllProjectResponse;
import com.example.cabonerfbe.services.ProcessService;
import com.example.cabonerfbe.services.ProjectService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectImpactValueRepository projectImpactValueRepository;
    @Autowired
    private ImpactMethodCategoryRepository impactMethodCategoryRepository;
    @Autowired
    private ProjectConverter projectConverter;
    @Autowired
    private ProjectImpactValueConverter projectImpactValueConverter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodConverter methodConverter;
    @Autowired
    private ImpactCategoryConverter categoryConverter;
    @Autowired
    private UnitConverter unitConverter;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ProcessConverter processConverter;
    @Autowired
    private ProcessImpactValueRepository processImpactValueRepository;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private ExchangesConverter exchangesConverter;
    @Autowired
    private ConnectorRepository connectorRepository;
    @Autowired
    private ConnectorConverter connectorConverter;
    @Autowired
    private ProcessService processService;

    private static final int PAGE_INDEX_ADJUSTMENT = 1;

    private final ExecutorService executorService = Executors.newFixedThreadPool(17);

    @Override
    public List<Project> getProjectListByMethodId(UUID id) {
//        return projectRepository.getProjectLevelDetail(id);
        return null;
    }

    @Override
    public Optional<Project> getProjectById(UUID id) {
        return Optional.empty();
    }

    @Override
    public CreateProjectResponse createProject(UUID userId, CreateProjectRequest request) {

        if(userRepository.findById(userId).isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "User not exist");
        }

        if(workspaceRepository.findById(request.getWorkspaceId()).isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Workspace not exist");
        }

        if(methodRepository.findById(request.getMethodId()).isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Method not exist");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setLocation(request.getLocation());
        project.setUser(userRepository.findById(userId).get());
        project.setWorkspace(workspaceRepository.findById(request.getWorkspaceId()).get());
        project.setLifeCycleImpactAssessmentMethod(methodRepository.findById(request.getMethodId()).get());

        project = projectRepository.save(project);

//        List<ImpactMethodCategory> list = impactMethodCategoryRepository.findByMethod(request.getMethodId());
//        List<ProjectImpactValue> listValues = new ArrayList<>();
//        for(ImpactMethodCategory x:list){
//            ProjectImpactValue values = new ProjectImpactValue();
//            values.setProject(project);
//            values.setValue(0);
//            values.setImpactMethodCategory(x);
//            listValues.add(values);
//        }
//
//        projectImpactValueRepository.saveAll(listValues);
        return CreateProjectResponse.builder()
                .projectId(project.getId())
                .build();
    }

    @Override
    public GetAllProjectResponse getAllProject(int pageCurrent, int pageSize, UUID userId, UUID methodId) {

        Pageable pageable = PageRequest.of(pageCurrent - PAGE_INDEX_ADJUSTMENT, pageSize);

        Page<Project> projects = null;
        if (methodId == null) {
            projects = projectRepository.findAll(userId, pageable);
        } else {
            projects = projectRepository.sortByMethod(userId, methodId, pageable);
        }

        if (projects.isEmpty()) {
            GetAllProjectResponse response = new GetAllProjectResponse();
            response.setPageCurrent(0);
            response.setPageSize(0);
            response.setTotalPage(0);
            response.setProjects(null);
            return response;
        }

        int totalPage = projects.getTotalPages();
        if (pageCurrent > totalPage) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("currentPage", MessageConstants.CURRENT_PAGE_EXCEED_TOTAL_PAGES));
        }


        List<ProjectDto> list = new ArrayList<>();
        for (Project project : projects) {
            ProjectDto projectDto = projectConverter.toDto(project);

            projectDto.setImpacts(converterProject(projectImpactValueRepository.findAllByProjectId(project.getId())));

            list.add(projectDto);
        }

        GetAllProjectResponse response = new GetAllProjectResponse();
        response.setPageCurrent(pageCurrent);
        response.setPageSize(pageSize);
        response.setTotalPage(totalPage);
        response.setProjects(list);


        return response;
    }

    @Override
    public GetProjectByIdDto getById(UUID id, UUID workspaceId) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND)
        );

        if (workspaceId == null) {
            throw CustomExceptions.unauthorized("workspace not exist");
        }

        return getProject(project);
    }

    @NotNull
    private GetProjectByIdDto getProject(Project project) {
        GetProjectByIdDto dto = new GetProjectByIdDto();

        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setLocation(project.getLocation());
        dto.setMethod(methodConverter.fromMethodToMethodDto(project.getLifeCycleImpactAssessmentMethod()));
        dto.setImpacts(converterProject(projectImpactValueRepository.findAllByProjectId(project.getId())));
        dto.setProcesses(processService.getAllProcessesByProjectId(project.getId()));
        dto.setConnectors(connectorConverter.fromListConnectorToConnectorDto(connectorRepository.findAllByProject(project.getId())));
        return dto;
    }

    @Override
    public UpdateProjectDto updateDetail(UUID id, UpdateProjectDetailRequest request) {
        if ((Objects.isNull(request.getName()) || request.getName().isEmpty())
                && (Objects.isNull(request.getDescription()) || request.getDescription().isEmpty())
                && (Objects.isNull(request.getLocation()) || request.getLocation().isEmpty())) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Update at least 1 field");
        }

        Optional<Project> p = projectRepository.findById(id);
        if (p.isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Project not exist");
        }

        if (!Objects.isNull(request.getName()) && !request.getName().isEmpty()) {
            p.get().setName(request.getName());
        }
        if (!Objects.isNull(request.getDescription()) && !request.getDescription().isEmpty()) {
            p.get().setDescription(request.getDescription());
        }
        if (!Objects.isNull(request.getLocation()) && !request.getLocation().isEmpty()) {
            p.get().setLocation(request.getLocation());
        }


        return projectConverter.fromDetailToDto(projectRepository.save(p.get()));
    }

    @Override
    public List<Project> deleteProject(UUID id) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty()){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR,"Project not exist");
        }

        project.get().setStatus(false);
        projectRepository.save(project.get());
        return new ArrayList<>();
    }

    @Override
    public GetProjectByIdDto changeProjectMethod(UUID projectId, UUID methodId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND)
        );
        if (!methodId.equals(project.getLifeCycleImpactAssessmentMethod().getId())) {
            LifeCycleImpactAssessmentMethod method = methodRepository.findByIdAndStatus(methodId, Constants.STATUS_TRUE).orElseThrow(
                    () -> CustomExceptions.badRequest(MessageConstants.NO_IMPACT_METHOD_FOUND)
            );
            project.setLifeCycleImpactAssessmentMethod(method);
            projectRepository.save(project);
        }

        return getProject(project);
    }

    public List<ProjectImpactDto> converterProject(List<ProjectImpactValue> list) {

        List<CompletableFuture<ProjectImpactDto>> futures = list.stream()
                .map(x -> CompletableFuture.supplyAsync(() -> {
                    ProjectImpactDto p = new ProjectImpactDto();
                    p.setId(x.getId());
                    p.setValue(x.getValue());
                    p.setMethod(methodConverter.fromMethodToMethodDto(
                            x.getImpactMethodCategory().getLifeCycleImpactAssessmentMethod()));
                    p.setImpactCategory(categoryConverter.fromProjectToImpactCategoryDto(
                            x.getImpactMethodCategory().getImpactCategory()));
                    return p;
                }, executorService))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.join();

        List<ProjectImpactDto> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        executorService.shutdown();
        return result;
    }


}
