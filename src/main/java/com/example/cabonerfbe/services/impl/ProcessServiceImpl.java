package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.GetAllProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;
import com.example.cabonerfbe.services.ProcessService;
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
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    ProcessRepository processRepository;
    @Autowired
    ProcessImpactValueRepository processImpactValueRepository;
    @Autowired
    LifeCycleStageRepository lifeCycleStageRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ImpactMethodCategoryRepository impactMethodCategoryRepository;
    @Autowired
    ProcessConverter processConverter;
    @Autowired
    ProcessImpactValueConverter processImpactValueConverter;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private ExchangesConverter exchangesConverter;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodConverter methodConverter;
    @Autowired
    private ImpactCategoryConverter categoryConverter;
    @Autowired
    private UnitConverter unitConverter;

    private final ExecutorService executorService = Executors.newFixedThreadPool(50);

    @Override
    public ProcessDetailDto createProcess(CreateProcessRequest request) {
        Process process = new Process();
        process.setName(request.getName());

        if (processRepository.findById(request.getId()).isPresent()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Process with id " + request.getId() + " already exists");
        }

        process.setId(request.getId());
        if (lifeCycleStageRepository.findById(request.getLifeCycleStagesId()).isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Life cycle stage not exist");
        }
        process.setLifeCycleStage(lifeCycleStageRepository.findById(request.getLifeCycleStagesId()).get());
        Optional<Project> project = projectRepository.findById(request.getProjectId());
        if (project.isEmpty()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, Map.of("projectId", "Not exist"));
        }
        process.setProject(projectRepository.findById(request.getProjectId()).get());
        process.setOverAllProductFlowRequired(0);

        process = processRepository.save(process);

//        List<ImpactMethodCategory> list = impactMethodCategoryRepository.findByMethod(project.get().getLifeCycleImpactAssessmentMethod().getId());
//        List<ProcessImpactValue> processImpactValues = new ArrayList<>();
//
//
//        for(ImpactMethodCategory x: list){
//            ProcessImpactValue piv = new ProcessImpactValue();
//            Random random = new Random();
//            piv.setUnitLevel(random.nextDouble());
//            piv.setProcess(process);
//            piv.setImpactMethodCategory(x);
//            piv.setSystemLevel(random.nextDouble());
//            piv.setOverallImpactContribution(0);
//            processImpactValues.add(piv);
//        }
//
//        processImpactValueRepository.saveAll(processImpactValues);

        return processConverter.INSTANCE.fromProcessDetailToProcessDto(process);
    }

    @Override
    public ProcessDto getProcessById(UUID id) {
        Optional<Process> process = processRepository.findByProcessId(id);
        if (process.isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Process not exist");
        }

        ProcessDto dto = processConverter.fromProcessToProcessDto(process.get());

        dto.setImpacts(converterProcess(processImpactValueRepository.findByProcessId(process.get().getId())));
        dto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(process.get().getId())));

        return dto;
    }

    @Override
    public List<ProcessDto> getAllProcesses(UUID projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Project not exist");
        }
        List<ProcessDto> processDtos = processRepository.findAll(projectId).stream().map(processConverter::fromProcessToProcessDto).collect(Collectors.toList());

        if (processDtos.isEmpty()) {
            return processDtos;
        }
        for (ProcessDto x : processDtos) {
            x.setImpacts(converterProcess(processImpactValueRepository.findByProcessId(x.getId())));
            x.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(x.getId())));
        }

        return processDtos;
    }

    @Override
    public ProcessDetailDto updateProcess(UUID id, UpdateProcessRequest request) {
        Optional<Process> process = processRepository.findByProcessId(id);
        if (process.isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Process not exist");
        }
        Optional<LifeCycleStage> lifeCycleStage = lifeCycleStageRepository.findById(request.getLifeCycleStagesId());

        if (lifeCycleStage.isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Life cycle stage not found");
        }

        process.get().setLifeCycleStage(lifeCycleStage.get());
        process.get().setDescription(request.getDescription());
        process.get().setName(request.getName());

        return processConverter.fromProcessDetailToProcessDto(processRepository.save(process.get()));
    }

    @Override
    public String deleteProcess(UUID id) {
        Optional<Process> process = processRepository.findByProcessId(id);
        if (process.isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Process not exist");
        }
        process.get().setStatus(false);
        processRepository.save(process.get());
        return "[]";
    }


    private List<ProcessImpactValueDto> converterProcess(List<ProcessImpactValue> list) {

        List<CompletableFuture<ProcessImpactValueDto>> futures = list.stream()
                .map(x -> CompletableFuture.supplyAsync(() -> {
                    ProcessImpactValueDto p = new ProcessImpactValueDto();
                    p.setId(x.getId());
                    p.setSystemLevel(x.getSystemLevel());
                    p.setUnitLevel(x.getUnitLevel());
                    p.setOverallImpactContribution(x.getOverallImpactContribution());
                    p.setMethod(methodConverter.fromMethodToMethodDto(x.getImpactMethodCategory().getLifeCycleImpactAssessmentMethod()));
                    p.setImpactCategory(categoryConverter.fromProjectToImpactCategoryDto(x.getImpactMethodCategory().getImpactCategory()));
                    return p;
                }, executorService))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFuture.join();

        List<ProcessImpactValueDto> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return result;
    }
}
