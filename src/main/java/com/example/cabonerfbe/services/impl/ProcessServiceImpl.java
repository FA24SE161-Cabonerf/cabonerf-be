package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.converter.LifeCycleImpactAssessmentMethodConverter;
import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import com.example.cabonerfbe.dto.ProcessNodeDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProcessImpactValueRequest;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.DeleteProcessResponse;
import com.example.cabonerfbe.services.MessagePublisher;
import com.example.cabonerfbe.services.ProcessService;
import com.example.cabonerfbe.services.UnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class Process service.
 *
 * @author SonPHH.
 */
@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;
    private final ProcessImpactValueRepository processImpactValueRepository;
    private final LifeCycleStageRepository lifeCycleStageRepository;
    private final ProjectRepository projectRepository;
    private final ProcessConverter processConverter;
    private final ExchangesRepository exchangesRepository;
    private final ExchangesConverter exchangesConverter;
    private final LifeCycleImpactAssessmentMethodConverter methodConverter;
    private final ImpactCategoryConverter categoryConverter;
    private final MessagePublisher messagePublisher;
    private final ConnectorServiceImpl connectorService;
    private final ConnectorRepository connectorRepository;
    private final UnitService unitService;
    private final UnitRepository unitRepository;
    private final ImpactMethodCategoryRepository impactMethodCategoryRepository;
    private final ProjectImpactValueRepository projectImpactValueRepository;
    private final EmissionSubstanceRepository emissionSubstanceRepository;

    /**
     * Instantiates a new Process service.
     *
     * @param processConverter               the process converter
     * @param processRepository              the process repository
     * @param processImpactValueRepository   the process impact value repository
     * @param lifeCycleStageRepository       the life cycle stage repository
     * @param projectRepository              the project repository
     * @param exchangesRepository            the exchanges repository
     * @param connectorRepository            the connector repository
     * @param exchangesConverter             the exchanges converter
     * @param unitService                    the unit service
     * @param methodConverter                the method converter
     * @param unitRepository                 the unit repository
     * @param categoryConverter              the category converter
     * @param messagePublisher               the message publisher
     * @param connectorService               the connector service
     * @param impactMethodCategoryRepository the impact method category repository
     * @param projectImpactValueRepository   the project impact value repository
     */
    @Autowired
    public ProcessServiceImpl(ProcessConverter processConverter, ProcessRepository processRepository, ProcessImpactValueRepository processImpactValueRepository, LifeCycleStageRepository lifeCycleStageRepository, ProjectRepository projectRepository, ExchangesRepository exchangesRepository, ConnectorRepository connectorRepository, ExchangesConverter exchangesConverter, UnitService unitService, LifeCycleImpactAssessmentMethodConverter methodConverter, UnitRepository unitRepository, ImpactCategoryConverter categoryConverter, MessagePublisher messagePublisher, ConnectorServiceImpl connectorService, ImpactMethodCategoryRepository impactMethodCategoryRepository, ProjectImpactValueRepository projectImpactValueRepository, EmissionSubstanceRepository emissionSubstanceRepository) {
        this.processConverter = processConverter;
        this.processRepository = processRepository;
        this.processImpactValueRepository = processImpactValueRepository;
        this.lifeCycleStageRepository = lifeCycleStageRepository;
        this.projectRepository = projectRepository;
        this.exchangesRepository = exchangesRepository;
        this.connectorRepository = connectorRepository;
        this.exchangesConverter = exchangesConverter;
        this.unitService = unitService;
        this.methodConverter = methodConverter;
        this.unitRepository = unitRepository;
        this.categoryConverter = categoryConverter;
        this.messagePublisher = messagePublisher;
        this.connectorService = connectorService;
        this.impactMethodCategoryRepository = impactMethodCategoryRepository;
        this.projectImpactValueRepository = projectImpactValueRepository;
        this.emissionSubstanceRepository = emissionSubstanceRepository;
    }

//    private final ExecutorService executorService = Executors.newFixedThreadPool(17);

    private static Map<UUID, BigDecimal> aggregateNet(ProcessNodeDto root) {
        Map<UUID, BigDecimal> result = new HashMap<>();
        aggregateNetRecursive(root, result);
        return result;
    }

    private static void aggregateNetRecursive(ProcessNodeDto node, Map<UUID, BigDecimal> result) {
        if (node == null) return;

        result.put(
                node.getProcessId(),
                result.getOrDefault(node.getProcessId(), BigDecimal.ZERO).add(node.getNet())
        );

        for (ProcessNodeDto subProcess : node.getSubProcesses()) {
            aggregateNetRecursive(subProcess, result);
        }
    }

    @Override
    public ProcessDto createProcess(CreateProcessRequest request) {
        LifeCycleStage lifeCycleStage = lifeCycleStageRepository.findByIdAndStatus(request.getLifeCycleStageId(), Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_LIFE_CYCLE_STAGE_FOUND, Collections.EMPTY_LIST)
        );


        Project project = projectRepository.findByIdAndStatusTrue(request.getProjectId()).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST)
        );

        // limit 20 processes in 1 project
        if (processRepository.countAllByProject_Id(project.getId())) {
            throw CustomExceptions.badRequest(MessageConstants.MAX_PROCESS_EXCEEDED);
        }

        Process process = new Process();
        process.setName(request.getName());
        process.setLifeCycleStage(lifeCycleStage);
        process.setProject(project);
        process.setMethodId(project.getLifeCycleImpactAssessmentMethod().getId());
        process.setOverAllProductFlowRequired(Constants.NEW_OVERALL_FLOW);
        process.setLibrary(false);
        process = processRepository.save(process);


        Process p = processRepository.findByProcessId(process.getId()).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROCESS_FOUND));

        // generate process impact value
        messagePublisher.publishCreateProcessImpactValue(RabbitMQConfig.CREATE_PROCESS_EXCHANGE, RabbitMQConfig.CREATE_PROCESS_ROUTING_KEY, process.getId(), project.getLifeCycleImpactAssessmentMethod().getId());

        ProcessDto processDto = processConverter.fromProcessToProcessDto(process);
        processDto.setImpacts(new ArrayList<>());
        processDto.setExchanges(new ArrayList<>());
        return processDto;
    }

    /**
     * Process impact value generate upon create process method.
     *
     * @param request the request
     */
    @RabbitListener(queues = RabbitMQConfig.CREATE_PROCESS_QUEUE)
    public void processImpactValueGenerateUponCreateProcess(CreateProcessImpactValueRequest request) {
        handleImpactValueCreation(request);
    }

    /**
     * Handle impact value creation method.
     *
     * @param request the request
     */
    public void handleImpactValueCreation(CreateProcessImpactValueRequest request) {
        UUID processId = request.getProcessId();
        UUID methodId = request.getMethodId();
        Process process = processRepository.findByProcessId(processId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROCESS_FOUND));
        List<ImpactMethodCategory> methodCategoryList = impactMethodCategoryRepository.findByMethod(methodId);
        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();
        for (ImpactMethodCategory methodCategory : methodCategoryList) {
            ProcessImpactValue processImpactValue = createNewProcessImpactValue(process, methodCategory);
            processImpactValueList.add(processImpactValue);
        }
        processImpactValueRepository.saveAll(processImpactValueList);
    }

    @Override
    public ProcessImpactValue createNewProcessImpactValue(Process process, ImpactMethodCategory methodCategory) {
        ProcessImpactValue processImpactValue = new ProcessImpactValue();
        processImpactValue.setProcess(process);
        processImpactValue.setImpactMethodCategory(methodCategory);
        processImpactValue.setOverallImpactContribution(Constants.DEFAULT_OVERALL_IMPACT_CONTRIBUTION);
        processImpactValue.setPreviousProcessValue(Constants.DEFAULT_PREVIOUS_PROCESS_VALUE);
        processImpactValue.setSystemLevel(Constants.DEFAULT_SYSTEM_LEVEL);
        processImpactValue.setUnitLevel(Constants.BASE_UNIT_LEVEL);
        processImpactValue.setVersion(Constants.VERSION_ONE);
        return processImpactValue;
    }

    @Override
    public ProcessDto getProcessById(UUID id) {
        Process process = processRepository.findByProcessId(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND, Collections.EMPTY_LIST)
        );
        ProcessDto dto = processConverter.fromProcessToProcessDto(process);

        dto.setImpacts(converterProcess(processImpactValueRepository.findByProcessId(process.getId())));
        dto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(process.getId())));

        return dto;
    }

    @Override
    public List<ProcessDto> getAllProcessesByProjectId(UUID projectId) {
        Project project = projectRepository.findByIdAndStatusTrue(projectId).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST)
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
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND, Collections.EMPTY_LIST)
        );

        if (process.isLibrary()) {
            throw CustomExceptions.badRequest(MessageConstants.CANNOT_EDIT_OBJECT_LIBRARY_PROCESS);
        }

        LifeCycleStage lifeCycleStage = lifeCycleStageRepository.findByIdAndStatus(request.getLifeCycleStagesId(), Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_LIFE_CYCLE_STAGE_FOUND, Collections.EMPTY_LIST)
        );

        process.setLifeCycleStage(lifeCycleStage);
        process.setDescription(request.getDescription());
        process.setName(request.getName());

        return processConverter.fromProcessDetailToProcessDto(processRepository.save(process));
    }

    @Override
    public DeleteProcessResponse deleteProcess(UUID id) {
        Process process = processRepository.findByProcessId(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND, Collections.EMPTY_LIST)
        );
        process.setStatus(false);
        processRepository.save(process);

        Thread deleteConnectorThread = new Thread(() -> connectorService.deleteAssociatedConnectors(id, Constants.DELETE_CONNECTOR_TYPE_PROCESS));
        deleteConnectorThread.start();
        return new DeleteProcessResponse(process.getId());
    }

    @Override
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

    /**
     * Create process listener method.
     *
     * @param request the request
     */
//    @RabbitListener(queues = RabbitMQConfig.CREATE_PROCESS_QUEUE)
    public void createProcessListener(CreateProcessRequest request) {
        ProcessDto processDto = createProcess(request);
        messagePublisher.publishCreateProcess(RabbitMQConfig.CREATED_PROCESS_EXCHANGE, RabbitMQConfig.CREATED_PROCESS_ROUTING_KEY, processDto);
    }

    @Override
    public ProcessNodeDto constructListProcessNodeDto(UUID projectId) {

        List<Process> processList = processRepository.findAll(projectId);
        List<Connector> connectorList = connectorRepository.findAllByProject(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_FOUND, Collections.EMPTY_LIST);
        }

        if (processList.size() == 1) {
            return buildTree(processList.get(0).getId(), connectorList, BigDecimal.ONE);
        }

        if (connectorList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_CONNECTOR_TO_CALCULATE, Collections.EMPTY_LIST);
        }

        if (connectorList.size() + 1 < processList.size()) {
            throw CustomExceptions.badRequest(MessageConstants.PROCESS_WITH_NO_CONNECTOR_ERROR, Collections.EMPTY_LIST);
        }

        List<Process> rootProcesses = processRepository.findRootProcess(projectId);
        if (rootProcesses.size() != 1) {
            throw CustomExceptions.badRequest(MessageConstants.MUST_BE_ONLY_ONE_FINAL_PROCESS);
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
                Unit defaultUnit = unitRepository.findDefault(connector.getEndExchanges().getUnit().getUnitGroup().getId());
                BigDecimal newNet = currentNet.multiply(calculateNet(connector, defaultUnit));

                ProcessNodeDto childTree = buildTree(startProcessId, connectors, newNet);
                children.add(childTree);
            }
        }

        nodeTree.setSubProcesses(children);

        return nodeTree;
    }

    private BigDecimal calculateNet(Connector connector, Unit defaultUnit) {
        // Example calculation based on the connector's start and end process or exchanges
        BigDecimal baseNet = BigDecimal.ONE;
        BigDecimal endValue = connector.getEndExchanges().getValue();
        BigDecimal startValue = connector.getStartExchanges().getValue();

        if (startValue.compareTo(BigDecimal.ZERO) == 0) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION + "- Process " + connector.getStartProcess().getName() + "'s product value cannot be 0.");
        }

        if (endValue.compareTo(BigDecimal.ZERO) == 0) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION + "- Process " + connector.getEndProcess().getName() + "'s product value cannot be 0.");
        }

        if (!connector.getEndExchanges().getUnit().getIsDefault()) {
            endValue = unitService.convertValue(connector.getEndExchanges().getUnit(), endValue, defaultUnit);
        }

        if (!connector.getStartExchanges().getUnit().getIsDefault()) {
            startValue = unitService.convertValue(connector.getStartExchanges().getUnit(), startValue, defaultUnit);
        }

        baseNet = baseNet.multiply(endValue.divide(startValue, MathContext.DECIMAL128));
        return baseNet;
    }

    @Override
    public void convertProcessToObjectLibrary(Process process, List<ProjectImpactValue> projectImpactValueList) {
        Process newProcess = createLibraryProcess(process);
        processRepository.save(newProcess);

        List<Exchanges> exchangesList = copyExchangesFromProcessToObj(process.getId(), newProcess, process.getProject().getId());
        exchangesRepository.saveAll(exchangesList);

        List<ProcessImpactValue> impactValues = copyProjectImpactValues(projectImpactValueList, newProcess);
        processImpactValueRepository.saveAll(impactValues);
    }

    private Process createLibraryProcess(Process process) {
        Process newProcess = new Process();
        newProcess.setLibrary(true);
        newProcess.setName(process.getName());
        newProcess.setDescription(process.getDescription());
        newProcess.setProject(null);
        newProcess.setMethodId(process.getMethodId());
        newProcess.setSystemBoundary(process.getProject().getSystemBoundary());
        newProcess.setLifeCycleStage(process.getLifeCycleStage());
        newProcess.setOrganization(process.getProject().getOrganization());
        newProcess.setOverAllProductFlowRequired(Constants.DEFAULT_OVERALL_PRODUCT_FLOW_REQUIRED);
        return newProcess;
    }

    private List<Exchanges> copyExchangesFromProcessToObj(UUID processId, Process newProcess, UUID projectId) {
        // find for all project
        // -> if processId = input => gets only the output
        List<Exchanges> elementaryList = exchangesRepository.findElementaryExchangeByProject(projectId);

        Map<UUID, BigDecimal> inputMap = new HashMap<>();
        Map<UUID, BigDecimal> outputMap = new HashMap<>();
        ExchangesType exchangesType = elementaryList.get(0).getExchangesType();

        elementaryList.forEach(e -> {
            UUID substanceId = e.getEmissionSubstance().getId();
            Unit exchangeUnit = e.getUnit();
            BigDecimal exchangeValue = e.getValue().multiply(e.getProcess().getOverAllProductFlowRequired());
            Unit baseUnit = e.getEmissionSubstance().getUnit();

            if (e.isInput()) {
                inputMap.merge(substanceId, unitService.convertValue(exchangeUnit, exchangeValue, baseUnit), BigDecimal::add);
            } else {
                outputMap.merge(substanceId, unitService.convertValue(exchangeUnit, exchangeValue, baseUnit), BigDecimal::add);
            }
        });

        List<Exchanges> newExchangeList = new ArrayList<>();
        List<Exchanges> inputList = inputMap.entrySet().stream()
                .map(entry -> createNewExchange(entry.getKey(), entry.getValue(), newProcess, exchangesType, true))
                .toList();

        List<Exchanges> outputList = outputMap.entrySet().stream()
                .map(entry -> createNewExchange(entry.getKey(), entry.getValue(), newProcess, exchangesType, false))
                .toList();

        newExchangeList.add(exchangesRepository.findProductOutWithOneProcess(processId));
        newExchangeList.addAll(inputList);
        newExchangeList.addAll(outputList);

        return newExchangeList;
    }

    private Exchanges createNewExchange(UUID substanceId, BigDecimal value, Process newProcess, ExchangesType exchangesType, boolean isInput) {
        EmissionSubstance emissionSubstance = emissionSubstanceRepository.findById(substanceId).orElseThrow(() ->
                CustomExceptions.badRequest(MessageConstants.NO_EMISSION_SUBSTANCE_FOUND));
        Exchanges newExchange = new Exchanges();
        newExchange.setProcess(newProcess);
        newExchange.setName(emissionSubstance.getSubstance().getName());
        newExchange.setDescription("");
        newExchange.setUnit(emissionSubstance.getUnit());
        newExchange.setEmissionSubstance(emissionSubstance);
        newExchange.setExchangesType(exchangesType);
        newExchange.setValue(value);
        newExchange.setInput(isInput);
        return newExchange;
    }

    private List<Exchanges> copyExchanges(UUID processId, Process newProcess) {
        List<Exchanges> exchanges = exchangesRepository.findAllByProcess(processId);
        boolean containsProductOutput = exchanges.stream()
                .anyMatch(e -> (isProductOutput(e) && !(e.getValue().compareTo(BigDecimal.ZERO) == 0)));

        if (!containsProductOutput) {
            throw CustomExceptions.badRequest(MessageConstants.CALCULATE_PROJECT_AGAIN);
        }

        return exchanges.stream()
                .filter(this::isNonProductInput)
                .map(e -> mapToNewExchange(e, newProcess))
                .toList();
    }

    private boolean isNonProductInput(Exchanges exchange) {
        return !(Constants.PRODUCT_EXCHANGE.equals(exchange.getExchangesType().getName()) && exchange.isInput());
    }

    private boolean isProductOutput(Exchanges exchange) {
        return (Constants.PRODUCT_EXCHANGE.equals(exchange.getExchangesType().getName()) && !exchange.isInput());
    }

    private Exchanges mapToNewExchange(Exchanges oldExchange, Process newProcess) {
        Exchanges newExchange = new Exchanges();
        newExchange.setName(oldExchange.getName());
        newExchange.setDescription(oldExchange.getDescription());
        newExchange.setValue(oldExchange.getValue());
        newExchange.setExchangesType(oldExchange.getExchangesType());
        newExchange.setProcess(newProcess);
        newExchange.setUnit(oldExchange.getUnit());
        newExchange.setInput(oldExchange.isInput());
        newExchange.setEmissionSubstance(oldExchange.getEmissionSubstance());
        return newExchange;
    }

    private List<ProcessImpactValue> copyProcessImpactValues(UUID processId, Process newProcess) {
        return processImpactValueRepository.findByProcessId(processId).stream()
                .map(oldValue -> mapToNewProcessImpactValue(oldValue, newProcess))
                .toList();
    }

    private List<ProcessImpactValue> copyProjectImpactValues(List<ProjectImpactValue> projectImpactValueList, Process newProcess) {
        return projectImpactValueList.stream()
                .map(projectValue -> mapToNewProcessImpactValue(projectValue, newProcess))
                .toList();
    }

    private ProcessImpactValue mapToNewProcessImpactValue(ProcessImpactValue oldValue, Process newProcess) {
        ProcessImpactValue newImpactValue = new ProcessImpactValue();
        newImpactValue.setImpactMethodCategory(oldValue.getImpactMethodCategory());
        newImpactValue.setProcess(newProcess);
        newImpactValue.setUnitLevel(oldValue.getUnitLevel());
        newImpactValue.setSystemLevel(Constants.DEFAULT_SYSTEM_LEVEL);
        newImpactValue.setOverallImpactContribution(Constants.DEFAULT_OVERALL_IMPACT_CONTRIBUTION);
        newImpactValue.setPreviousProcessValue(Constants.DEFAULT_PREVIOUS_PROCESS_VALUE);
        return newImpactValue;
    }

    private ProcessImpactValue mapToNewProcessImpactValue(ProjectImpactValue projectImpactValue, Process newProcess) {
        ProcessImpactValue newImpactValue = new ProcessImpactValue();
        newImpactValue.setImpactMethodCategory(projectImpactValue.getImpactMethodCategory());
        newImpactValue.setProcess(newProcess);
        newImpactValue.setUnitLevel(projectImpactValue.getValue());
        newImpactValue.setSystemLevel(Constants.DEFAULT_SYSTEM_LEVEL);
        newImpactValue.setOverallImpactContribution(Constants.DEFAULT_OVERALL_IMPACT_CONTRIBUTION);
        newImpactValue.setPreviousProcessValue(Constants.DEFAULT_PREVIOUS_PROCESS_VALUE);
        return newImpactValue;
    }

    @Transactional
    @Override
    public ProcessDto convertObjectLibraryToProcessDto(Process object, Project project) {
        Process newProcess = createProcessFromLibrary(object, project);
        processRepository.save(newProcess);

        List<Exchanges> exchangesList = copyExchanges(object.getId(), newProcess);
        exchangesRepository.saveAll(exchangesList);
        exchangesList = exchangesList.stream()
                .filter(e -> e.getExchangesType().getName()
                        .equals(Constants.PRODUCT_EXCHANGE) && !e.isInput())
                .toList();

        List<ProcessImpactValue> impactValues = copyProcessImpactValues(object.getId(), newProcess);
        processImpactValueRepository.saveAll(impactValues);

        return buildProcessDto(newProcess, exchangesList, impactValues);
    }

    private Process createProcessFromLibrary(Process object, Project project) {
        Process newProcess = new Process();
        newProcess.setName(object.getName());
        newProcess.setDescription(object.getDescription());
        newProcess.setMethodId(object.getMethodId());
        newProcess.setLibrary(object.isLibrary());
        newProcess.setLifeCycleStage(object.getLifeCycleStage());
        newProcess.setOverAllProductFlowRequired(Constants.NEW_OVERALL_FLOW);
        newProcess.setProject(project);
        newProcess.setOrganization(project.getOrganization());
        return newProcess;
    }

    private ProcessDto buildProcessDto(Process newProcess, List<Exchanges> exchangesList, List<ProcessImpactValue> impactValues) {
        ProcessDto processDto = processConverter.fromProcessToProcessDto(newProcess);
        processDto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesList));
        processDto.setImpacts(converterProcess(impactValues));
        return processDto;
    }

    @Override
    public ProcessNodeDto calculationFast(UUID projectId) {
        ProcessNodeDto dto = constructListProcessNodeDto(projectId);
        Map<UUID, BigDecimal> totalNet = aggregateNet(dto);

        List<ProcessImpactValue> updatedImpactValues = totalNet.keySet().stream()
                .flatMap(processId -> processImpactValueRepository.findByProcessId(processId).stream()
                        .peek(x -> x.setSystemLevel(totalNet.get(processId).multiply(x.getUnitLevel()))))
                .toList();
        processImpactValueRepository.saveAll(updatedImpactValues);

        totalNet.forEach((processId, net) ->
                processRepository.findByProcessId(processId).ifPresent(data -> {
                    data.setOverAllProductFlowRequired(net);
                    processRepository.save(data);
                })
        );

        return dto;
    }

    /**
     * Calculate to designated process method.
     *
     * @param node the node
     * @return the list
     */
    public List<ProcessImpactValue> calculateToDesignatedProcess(ProcessNodeDto node) {
        Map<UUID, BigDecimal> totalNet = aggregateNet(node);

        List<ProcessImpactValue> updatedImpactValues = totalNet.keySet().stream()
                .flatMap(processId -> processImpactValueRepository.findByProcessId(processId).stream()
                        .peek(x -> x.setSystemLevel(totalNet.get(processId).multiply(x.getUnitLevel()))))
                .toList();

//        processImpactValueRepository.saveAll(updatedImpactValues);

        return updatedImpactValues.stream().filter(x -> x.getProcessId().equals(node.getProcessId())).collect(Collectors.toList());
    }
}
