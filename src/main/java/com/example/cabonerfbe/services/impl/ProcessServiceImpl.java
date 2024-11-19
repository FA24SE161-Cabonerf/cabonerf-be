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
        // Step 1: Retrieve connectors and root nodes
        List<Connector> connectorList = connectorRepository.findAllByProject(projectId);
        List<Process> rootNode = processRepository.findProcessesWithoutOutgoingConnectors(projectId);
        UUID rootNodeId = rootNode.get(0).getId();  // Assuming only one root node is present

        // Step 2: Construct linked list from the root node
        // Find the connectors that lead to the root node
        List<Connector> connectors = connectorRepository.findConnectorToProcess(rootNodeId);

        // Step 3: Iterate through connectors and create PathDto for each
        Map<String, List<PathDto>> contributions = new HashMap<>();

        for (Connector connector : connectors) {
            Process startProcess = connector.getStartProcess();
            Process endProcess = connector.getEndProcess();

            // Create or update the list of paths for the start process
            if (!contributions.containsKey(startProcess.getId().toString())) {
                contributions.put(startProcess.getId().toString(), new ArrayList<>());
            }

            // Step 4: Construct PathDto
            PathDto pathDto = new PathDto();
            pathDto.setDestinationNodeId(endProcess.getId().toString());
            pathDto.setNet(calculateNet(connector)); // Assuming calculateNet is a method you define to get the net contribution
            pathDto.setPath(buildPath(connector)); // Assuming buildPath is a method to build the path from start to destination

            // Add pathDto to the contributions map for the start process
            contributions.get(startProcess.getId().toString()).add(pathDto);
        }

        // Step 5: Create ProcessNodeDto objects for each root process and add to result
        ProcessNodeDto processNodeDto = new ProcessNodeDto();
        processNodeDto.setContributions(contributions);

        return processNodeDto;
    }

    private BigDecimal calculateNet(Connector connector) {
        // Example calculation based on the connector's start and end process or exchanges
        BigDecimal baseNet = BigDecimal.ONE;
        baseNet = baseNet.multiply(connector.getEndExchanges().getValue().divide(connector.getStartExchanges().getValue(), MathContext.DECIMAL128));
        return baseNet;
    }

    private List<String> buildPath(Connector connector) {
        List<String> path = new ArrayList<>();
        path.add(connector.getStartProcess().getId().toString());
        path.add(connector.getEndProcess().getId().toString());
        // You could expand this logic to handle more complex paths, e.g., handling multiple intermediary processes
        return path;
    }


}
