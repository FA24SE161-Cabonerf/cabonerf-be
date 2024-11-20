package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.converter.LifeCycleImpactAssessmentMethodConverter;
import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.DeleteProcessResponse;
import com.example.cabonerfbe.services.MessagePublisher;
import com.example.cabonerfbe.services.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
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
    ProcessConverter processConverter;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private ExchangesConverter exchangesConverter;
    @Autowired
    private LifeCycleImpactAssessmentMethodConverter methodConverter;
    @Autowired
    private ImpactCategoryConverter categoryConverter;
    @Autowired
    private MessagePublisher messagePublisher;
    @Autowired
    private ConnectorServiceImpl connectorService;
    @Autowired
    private ConnectorRepository connectorRepository;

//    private final ExecutorService executorService = Executors.newFixedThreadPool(17);

    @Override
    public ProcessDto createProcess(CreateProcessRequest request) {
        LifeCycleStage lifeCycleStage = lifeCycleStageRepository.findByIdAndStatus(request.getLifeCycleStageId(), Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_LIFE_CYCLE_STAGE_FOUND)
        );

        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND)
        );

        Process process = new Process();
        process.setName(request.getName());
        process.setLifeCycleStage(lifeCycleStage);
        process.setProject(project);
        process.setOverAllProductFlowRequired(Constants.NEW_OVERALL_FLOW);
        process = processRepository.save(process);

        // generate process impact value
        messagePublisher.publishCreateProcessImpactValue(RabbitMQConfig.CREATE_PROCESS_EXCHANGE, RabbitMQConfig.CREATE_PROCESS_ROUTING_KEY, process.getId(), project.getLifeCycleImpactAssessmentMethod().getId());

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
    public DeleteProcessResponse deleteProcess(UUID id) {
        Process process = processRepository.findByProcessId(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND)
        );
        process.setStatus(false);
        processRepository.save(process);

        Thread deleteConnectorThread = new Thread(() -> connectorService.deleteAssociatedConnectors(id, Constants.DELETE_CONNECTOR_TYPE_PROCESS));
        deleteConnectorThread.start();

        return new DeleteProcessResponse(process.getId());
    }

    public List<ProcessImpactValueDto> converterProcess(List<ProcessImpactValue> list) {
        return list.stream()
                .map(x -> {
                    ProcessImpactValueDto p = new ProcessImpactValueDto();
                    p.setId(x.getId());
                    p.setSystemLevel(x.getSystemLevel());
                    p.setUnitLevel(x.getUnitLevel());
                    p.setOverallImpactContribution(x.getOverallImpactContribution());
                    p.setMethod(methodConverter.fromMethodToMethodDto(x.getImpactMethodCategory().getLifeCycleImpactAssessmentMethod()));
                    p.setImpactCategory(categoryConverter.fromProjectToImpactCategoryDto(x.getImpactMethodCategory().getImpactCategory()));
                    return p;
                })
                .collect(Collectors.toList());
    }


    //    @RabbitListener(queues = RabbitMQConfig.CREATE_PROCESS_QUEUE)
    public void createProcessListener(CreateProcessRequest request) {
        log.info("create process message from nodeBased server: {}", request);
        ProcessDto processDto = createProcess(request);
        messagePublisher.publishCreateProcess(RabbitMQConfig.CREATED_PROCESS_EXCHANGE, RabbitMQConfig.CREATED_PROCESS_ROUTING_KEY, processDto);
    }

    public ProcessNodeDto constructListProcessNodeDto(UUID projectId) {
        log.info("constructing contribution breakdown data");

        List<Process> processList = processRepository.findAll(projectId);
        List<Connector> connectorList = connectorRepository.findAllByProject(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_FOUND);
        }

        if (processList.size() == 1) {
            return buildTree(processList.get(0).getId(), connectorList, BigDecimal.ONE);
        }

        if (connectorList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_CONNECTOR_TO_CALCULATE);
        }

        List<Process> rootProcesses = processRepository.findRootProcess(projectId);
        if (rootProcesses.size() != 1) {
            throw CustomExceptions.badRequest(MessageConstants.MUST_BE_ONLY_ONE_ROOT_PROCESS);
        }

        UUID rootNodeId = rootProcesses.get(0).getId();

        return buildTree(rootNodeId, connectorList, BigDecimal.ONE);
    }

    private ProcessNodeDto buildTree(UUID currentNodeId, List<Connector> connectors, BigDecimal currentNet) {
        ProcessNodeDto nodeTree = new ProcessNodeDto();
        nodeTree.setProcessId(currentNodeId);
        nodeTree.setNet(currentNet);

        List<ProcessNodeDto> children = new ArrayList<>();
        for (Connector connector : connectors) {
            if (connector.getEndProcess().getId().equals(currentNodeId)) {
                UUID startProcessId = connector.getStartProcess().getId();
                BigDecimal newNet = currentNet.multiply(calculateNet(connector));

                ProcessNodeDto childTree = buildTree(startProcessId, connectors, newNet);
                children.add(childTree);
            }
        }

        nodeTree.setSubProcesses(children);

        return nodeTree;
    }

    private BigDecimal calculateNet(Connector connector) {
        // Example calculation based on the connector's start and end process or exchanges
        BigDecimal baseNet = BigDecimal.ONE;
        baseNet = baseNet.multiply(connector.getEndExchanges().getValue().divide(connector.getStartExchanges().getValue(), MathContext.DECIMAL128));
        return baseNet;
    }

}
