package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.services.MessagePublisher;
import com.example.cabonerfbe.services.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    @Autowired
    private MessagePublisher messagePublisher;

    public static final double NEW_OVERALL_FLOW = 0;

    private final ExecutorService executorService = Executors.newFixedThreadPool(50);

    @Override
    public ProcessDto createProcess(CreateProcessRequest request) {
        LifeCycleStage lifeCycleStage = lifeCycleStageRepository.findByIdAndStatus(request.getLifeCycleStagesId(), Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_LIFE_CYCLE_STAGE_FOUND)
        );

        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND)
        );

        Process process = new Process();
        process.setName(request.getName());
        process.setLifeCycleStage(lifeCycleStage);
        process.setProject(project);
        process.setOverAllProductFlowRequired(NEW_OVERALL_FLOW);
        process = processRepository.save(process);

        ProcessDto processDto = processConverter.fromProcessToProcessDto(process);
        processDto.setImpacts(new ArrayList<>());
        processDto.setExchanges(new ArrayList<>());

        return processDto;
    }

    @Override
    public ProcessDto getProcessById(UUID id) {
        Process process = processRepository.findByProcessId(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND)
        );
        ProcessDto dto = processConverter.fromProcessToProcessDto(process);

        dto.setImpacts(converterProcess(processImpactValueRepository.findByProcessId(process.getId())));
        dto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(process.getId())));

        return dto;
    }

    @Override
    public List<ProcessDto> getAllProcessesByProjectId(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND)
        );
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
        Process process = processRepository.findByProcessId(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND)
        );

        LifeCycleStage lifeCycleStage = lifeCycleStageRepository.findByIdAndStatus(request.getLifeCycleStagesId(), Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_LIFE_CYCLE_STAGE_FOUND)
        );

        process.setLifeCycleStage(lifeCycleStage);
        process.setDescription(request.getDescription());
        process.setName(request.getName());

        return processConverter.fromProcessDetailToProcessDto(processRepository.save(process));
    }

    @Override
    public String deleteProcess(UUID id) {
        Process process = processRepository.findByProcessId(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND)
        );
        process.setStatus(false);
        processRepository.save(process);
        return process.getId().toString();
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
                .toList();

        CompletableFuture<Void> allFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFuture.join();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

//    @RabbitListener(queues = RabbitMQConfig.CREATE_PROCESS_QUEUE)
    public void createProcessListener(CreateProcessRequest request) {
        log.info("create process message from nodeBased server: {}", request);
        ProcessDto processDto = createProcess(request);
        messagePublisher.publishCreateProcess(RabbitMQConfig.CREATED_PROCESS_EXCHANGE, RabbitMQConfig.CREATED_PROCESS_ROUTING_KEY, processDto);
    }
}
