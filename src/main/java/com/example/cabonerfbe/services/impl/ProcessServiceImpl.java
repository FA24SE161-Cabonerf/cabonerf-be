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
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

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
    private final UnitServiceImpl unitService;
    private final UnitRepository unitRepository;
    private final ImpactMethodCategoryRepository impactMethodCategoryRepository;

    @Autowired
    public ProcessServiceImpl(ProcessConverter processConverter, ProcessRepository processRepository, ProcessImpactValueRepository processImpactValueRepository, LifeCycleStageRepository lifeCycleStageRepository, ProjectRepository projectRepository, ExchangesRepository exchangesRepository, ConnectorRepository connectorRepository, ExchangesConverter exchangesConverter, UnitServiceImpl unitService, LifeCycleImpactAssessmentMethodConverter methodConverter, UnitRepository unitRepository, ImpactCategoryConverter categoryConverter, MessagePublisher messagePublisher, ConnectorServiceImpl connectorService, ImpactMethodCategoryRepository impactMethodCategoryRepository) {
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
    }

//    private final ExecutorService executorService = Executors.newFixedThreadPool(17);

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

        System.out.println("process id nè: " + process.getId());

        Process p = processRepository.findByProcessId(process.getId()).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROCESS_FOUND));

        System.out.println("lấy lúc tạo nè: " + p);
        // generate process impact value
        messagePublisher.publishCreateProcessImpactValue(RabbitMQConfig.CREATE_PROCESS_EXCHANGE, RabbitMQConfig.CREATE_PROCESS_ROUTING_KEY, process.getId(), project.getLifeCycleImpactAssessmentMethod().getId());

        ProcessDto processDto = processConverter.fromProcessToProcessDto(process);
        processDto.setImpacts(new ArrayList<>());
        processDto.setExchanges(new ArrayList<>());
        return processDto;
    }

    @RabbitListener(queues = RabbitMQConfig.CREATE_PROCESS_QUEUE)
    public void processImpactValueGenerateUponCreateProcess(CreateProcessImpactValueRequest request) {
        System.out.println("dô tạo list impact nè với process id nè: " + request.getProcessId());
        handleImpactValueCreation(request);
    }

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


    //    @RabbitListener(queues = RabbitMQConfig.CREATE_PROCESS_QUEUE)
    public void createProcessListener(CreateProcessRequest request) {
        log.info("create process message from nodeBased server: {}", request);
        ProcessDto processDto = createProcess(request);
        messagePublisher.publishCreateProcess(RabbitMQConfig.CREATED_PROCESS_EXCHANGE, RabbitMQConfig.CREATED_PROCESS_ROUTING_KEY, processDto);
    }

    @Override
    public ProcessNodeDto constructListProcessNodeDto(UUID projectId) {
        log.info("constructing contribution breakdown data");

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
    public void convertProcessToObjectLibrary(Process process) {
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
        processRepository.save(newProcess);

        List<Exchanges> exchangesList = exchangesRepository.findAllByProcess(process.getId()).stream()
                .filter(x -> !(Constants.PRODUCT_EXCHANGE.equals(x.getExchangesType().getName()) && x.isInput())).map(
                        oldExchange -> {
                            Exchanges newExchanges = new Exchanges();
                            newExchanges.setName(oldExchange.getName());
                            newExchanges.setDescription(oldExchange.getDescription());
                            newExchanges.setValue(oldExchange.getValue());
                            newExchanges.setExchangesType(oldExchange.getExchangesType());
                            newExchanges.setProcess(newProcess);
                            newExchanges.setUnit(oldExchange.getUnit());
                            newExchanges.setInput(oldExchange.isInput());
                            newExchanges.setEmissionSubstance(oldExchange.getEmissionSubstance());
                            return newExchanges;
                        }
                ).toList();
        exchangesRepository.saveAll(exchangesList);


//        ProcessNodeDto tree = buildTree(process.getId(), connectorRepository.findConnectorToProcess(process.getId()), BigDecimal.ONE);
//        List<ProcessImpactValue> processImpactValueList = calculateToDesignatedProcess(tree);

        List<ProcessImpactValue> processImpactValueList = processImpactValueRepository.findByProcessId(process.getId()).stream()
                .map(oldValue -> {
                            ProcessImpactValue newImpactValue = new ProcessImpactValue();
                            newImpactValue.setImpactMethodCategory(oldValue.getImpactMethodCategory());
                            newImpactValue.setProcess(newProcess);
                            newImpactValue.setUnitLevel(oldValue.getSystemLevel());
                            newImpactValue.setSystemLevel(Constants.DEFAULT_SYSTEM_LEVEL);
                            newImpactValue.setOverallImpactContribution(Constants.DEFAULT_OVERALL_IMPACT_CONTRIBUTION);
                            newImpactValue.setPreviousProcessValue(Constants.DEFAULT_PREVIOUS_PROCESS_VALUE);
                            return newImpactValue;
                        }
                ).toList();

        processImpactValueRepository.saveAll(processImpactValueList);
    }

    @Transactional
    @Override
    public ProcessDto convertObjectLibraryToProcessDto(Process object, Project project) {
        Process newProcess = new Process();
        newProcess.setName(object.getName());
        newProcess.setDescription(object.getDescription());
        newProcess.setMethodId(object.getMethodId());
        newProcess.setLibrary(true);
        newProcess.setLifeCycleStage(object.getLifeCycleStage());
        newProcess.setOverAllProductFlowRequired(Constants.NEW_OVERALL_FLOW);
        newProcess.setProject(project);
        newProcess.setOrganization(project.getOrganization());
        newProcess = processRepository.save(newProcess);

        ProcessDto processDto = processConverter.fromProcessToProcessDto(newProcess);
        Process finalNewProcess = newProcess;
        List<Exchanges> exchangesList = exchangesRepository.findAllByProcess(object.getId()).stream()
                .map(objectExchange -> {
                            Exchanges newExchanges = new Exchanges();
                            newExchanges.setName(objectExchange.getName());
                            newExchanges.setDescription(objectExchange.getDescription());
                            newExchanges.setValue(objectExchange.getValue());
                            newExchanges.setExchangesType(objectExchange.getExchangesType());
                            newExchanges.setProcess(finalNewProcess);
                            newExchanges.setUnit(objectExchange.getUnit());
                            newExchanges.setInput(objectExchange.isInput());
                            newExchanges.setEmissionSubstance(objectExchange.getEmissionSubstance());
                            return newExchanges;
                        }
                ).toList();

        List<ProcessImpactValue> processImpactValueList = processImpactValueRepository.findByProcessId(object.getId()).stream()
                .map(objectValue -> {
                            ProcessImpactValue newImpactValue = new ProcessImpactValue();
                            newImpactValue.setImpactMethodCategory(objectValue.getImpactMethodCategory());
                            newImpactValue.setProcess(finalNewProcess);
                            newImpactValue.setUnitLevel(objectValue.getUnitLevel());
                            newImpactValue.setSystemLevel(Constants.DEFAULT_SYSTEM_LEVEL);
                            newImpactValue.setOverallImpactContribution(Constants.DEFAULT_OVERALL_IMPACT_CONTRIBUTION);
                            newImpactValue.setPreviousProcessValue(Constants.DEFAULT_PREVIOUS_PROCESS_VALUE);
                            return newImpactValue;
                        }
                ).toList();
        processDto.setImpacts(converterProcess(processImpactValueRepository.saveAll(processImpactValueList)));
        processDto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.saveAll(exchangesList)));

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

    public List<ProcessImpactValue> calculateToDesignatedProcess(ProcessNodeDto node) {
        Map<UUID, BigDecimal> totalNet = aggregateNet(node);

        List<ProcessImpactValue> updatedImpactValues = totalNet.keySet().stream()
                .flatMap(processId -> processImpactValueRepository.findByProcessId(processId).stream()
                        .peek(x -> x.setSystemLevel(totalNet.get(processId).multiply(x.getUnitLevel()))))
                .toList();

//        processImpactValueRepository.saveAll(updatedImpactValues);

        return updatedImpactValues.stream().filter(x -> x.getProcessId().equals(node.getProcessId())).collect(Collectors.toList());
    }


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
}
