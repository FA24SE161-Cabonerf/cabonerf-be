package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.converter.LifeCycleStageConverter;
import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProcessImpactValueRequest;
import com.example.cabonerfbe.services.ProcessImpactValueService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessImpactValueServiceImpl implements ProcessImpactValueService {
    @Autowired
    private SystemBoundaryRepository systemBoundaryRepository;
    private final List<ConnectorPercentDto> connectorsResponse = new ArrayList<>();
    private final ProjectImpactValue totalProject = new ProjectImpactValue();
    private final List<Connector> _connectors = new ArrayList<>();
    private final List<String> toGateList = Arrays.asList("End-of-life", "Use");

    @Autowired
    ProcessRepository processRepository;
    @Autowired
    ImpactMethodCategoryRepository impactMethodCategoryRepository;
    @Autowired
    ProcessImpactValueRepository processImpactValueRepository;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorsRepository midpointFactorsRepository;
    @Autowired
    private UnitServiceImpl unitService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ConnectorRepository connectorRepository;
    @Autowired
    private ProjectImpactValueRepository projectImpactValueRepository;
    private String exchangeIdNext = null;

    @Autowired
    private ProcessServiceImpl processService;

    @Autowired
    private LifeCycleStageRepository lcsRepository;
    @Autowired
    private LifeCycleStageConverter lcsConverter;
    @Autowired
    private ImpactCategoryRepository icRepository;
    @Autowired
    private ImpactCategoryConverter icConverter;

    @NotNull
    private static ProcessImpactValue getNewProcessImpactValue(ImpactMethodCategory methodCategory, Process process) {
        ProcessImpactValue processImpactValue = new ProcessImpactValue();
        processImpactValue.setProcess(process);
        processImpactValue.setImpactMethodCategory(methodCategory);
        processImpactValue.setOverallImpactContribution(Constants.DEFAULT_OVERALL_IMPACT_CONTRIBUTION);
        processImpactValue.setPreviousProcessValue(Constants.DEFAULT_PREVIOUS_PROCESS_VALUE);
        processImpactValue.setSystemLevel(Constants.DEFAULT_SYSTEM_LEVEL);
        processImpactValue.setUnitLevel(Constants.BASE_UNIT_LEVEL);
        return processImpactValue;
    }

    @RabbitListener(queues = RabbitMQConfig.CREATE_PROCESS_QUEUE)
    private void processImpactValueGenerateUponCreateProcess(CreateProcessImpactValueRequest request) {
        UUID processId = request.getProcessId();
        UUID methodId = request.getMethodId();
        Process process = processRepository.findByProcessId(processId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROCESS_FOUND));
        List<ImpactMethodCategory> methodCategoryList = impactMethodCategoryRepository.findByMethod(methodId);
        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();
        for (ImpactMethodCategory methodCategory : methodCategoryList) {
            ProcessImpactValue processImpactValue = getNewProcessImpactValue(methodCategory, process);
            processImpactValueList.add(processImpactValue);
        }
        processImpactValueRepository.saveAll(processImpactValueList);
    }

    // this one will be used once the client is too tired to update one by one.
    // however this still needs to be optimized
    public void computeProcessImpactValueAllExchangeOfProcess(Process process) {
        UUID processId = process.getId();
        // the idea here is:
        // -> loop through the exchange list ->
        // -> get the list of factors that exchange has.
        // -> if present => unit level +=
        // todo: use map then update all at once.
        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();
        List<Exchanges> exchangeList = exchangesRepository.findAllByProcessIdAndExchangesType(processId,
                Constants.ELEMENTARY_EXCHANGE);

        for (Exchanges exchange : exchangeList) {
            UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
            List<MidpointImpactCharacterizationFactors> list = midpointFactorsRepository
                    .findByEmissionSubstanceId(emissionSubstanceId);
            BigDecimal exchangeValue = exchange.getValue();
            Unit exchangeUnit = exchange.getUnit();
            Unit baseUnit = exchange.getEmissionSubstance().getUnit();
            for (MidpointImpactCharacterizationFactors factors : list) {
                Optional<ProcessImpactValue> processImpactValueOpt = processImpactValueRepository
                        .findByProcessIdAndImpactMethodCategoryId(
                                processId, factors.getImpactMethodCategory().getId());
                if (processImpactValueOpt.isPresent()) {
                    ProcessImpactValue processImpactValue = processImpactValueOpt.get();
                    BigDecimal unitLevel = processImpactValue.getUnitLevel();
                    BigDecimal factorValue = factors.getDecimalValue();
                    exchangeValue = unitService.convertValue(exchangeUnit, exchangeValue, baseUnit);
                    unitLevel = unitLevel.add(exchangeValue.multiply(factorValue));

                    processImpactValue.setUnitLevel(unitLevel);
                    processImpactValueList.add(processImpactValue);
                }
            }
        }

        processImpactValueRepository.saveAll(processImpactValueList);
    }

    public void computeProcessImpactValueSingleExchange(Process process, Exchanges exchange, BigDecimal initialValue) {
        UUID processId = process.getId();
        log.info("Starting impact value computation for process ID: " + processId);

        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();
        UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
        Unit baseUnit = exchange.getEmissionSubstance().getUnit();

        Optional<Exchanges> exchanges = exchangesRepository.findProductOut(processId);

        List<MidpointImpactCharacterizationFactors> list = midpointFactorsRepository
                .findByEmissionSubstanceId(emissionSubstanceId);

        for (MidpointImpactCharacterizationFactors factors : list) {
            Optional<ProcessImpactValue> processImpactValueOpt = processImpactValueRepository
                    .findByProcessIdAndImpactMethodCategoryId(
                            processId, factors.getImpactMethodCategory().getId());

            if (processImpactValueOpt.isPresent()) {
                ProcessImpactValue processImpactValue = processImpactValueOpt.get();
                BigDecimal unitLevel = processImpactValue.getUnitLevel();
                BigDecimal systemLevel = processImpactValue.getSystemLevel();
                BigDecimal totalFlow = exchanges.map(value -> process.getOverAllProductFlowRequired()).orElse(BigDecimal.ONE);
                System.out.println("Processing impact method category ID: " + factors.getImpactMethodCategory().getId());
                System.out.println("Initial unit level: " + unitLevel);
                System.out.println("base exchange value (before converted): " + exchange.getValue());
                System.out.println("initial value: " + initialValue);
                // Convert the exchange value to the base unit and adjust based on initial value
                BigDecimal exchangeValue = unitService.convertValue(
                        exchange.getUnit(),
                        exchange.getValue().subtract(initialValue),
                        baseUnit);
                System.out.println("Converted exchange value: " + exchangeValue);

                // Adjust unit level by adding the product of exchange value and factor
                BigDecimal factorValue = factors.getDecimalValue();
                unitLevel = unitLevel.add(exchangeValue.multiply(factorValue));
                systemLevel = systemLevel.add(exchangeValue.multiply(factorValue.multiply(totalFlow)));

                System.out.println("Factor value: " + factorValue);
                System.out.println("Updated unit level: " + unitLevel);

                processImpactValue.setUnitLevel(unitLevel);
                processImpactValue.setSystemLevel(systemLevel);
                processImpactValueList.add(processImpactValue);
            }
        }

        // Batch save processImpactValues in chunks
        int batchSize = 100;
        for (int i = 0; i < processImpactValueList.size(); i += batchSize) {
            List<ProcessImpactValue> batch = processImpactValueList.subList(i,
                    Math.min(i + batchSize, processImpactValueList.size()));
            processImpactValueRepository.saveAll(batch);
        }

    }

    public void computeProcessImpactValueOfProject(Project project) {
        // the idea here is getting all the process impact value based on the project id
        // input,
        // then update each manually, this might take like forever since each exchange
        // inside each process is calculated again
        // the issue here is performance -> 1 project may have many processes, and each
        // process can also have several exchanges
        UUID projectId = project.getId();
        UUID methodId = project.getLifeCycleImpactAssessmentMethod().getId();
        List<Process> processList = processRepository.findAll(projectId);
        for (Process process : processList) {
            // alter the old ones instead of generating new ones for that specific method.
            alterPrevImpactValueList(process, methodId);
            computeProcessImpactValueAllExchangeOfProcess(process);
        }
    }

    private void alterPrevImpactValueList(Process process, UUID methodId) {
        List<ImpactMethodCategory> methodCategories = impactMethodCategoryRepository.findByMethod(methodId);
        List<ProcessImpactValue> existingValues = processImpactValueRepository.findByProcessId(process.getId());

        for (int i = 0; i < methodCategories.size(); i++) {
            if (i < existingValues.size()) {
                existingValues.get(i).setImpactMethodCategory(methodCategories.get(i));
                existingValues.get(i).setUnitLevel(BigDecimal.ZERO);
            } else {
                existingValues.add(getNewProcessImpactValue(methodCategories.get(i), process));
            }
        }

        if (existingValues.size() > methodCategories.size()) {
            List<ProcessImpactValue> removeList = existingValues.subList(
                    methodCategories.size(),
                    existingValues.size());
            processImpactValueRepository.deleteAll(removeList);
            existingValues = existingValues.subList(0, methodCategories.size());
        }

        processImpactValueRepository.saveAll(existingValues);
    }

    public ProcessNodeDto computeSystemLevelOfProject(UUID projectId) {
        String sysBoundaryFrom = Constants.BOUNDARY_CRADLE;
        String sysBoundaryTo = Constants.BOUNDARY_GATE;

        Project project = projectRepository.findByIdAndStatusTrue(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST));

        // Lấy toàn bộ process và kiểm tra
        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_IN_PROJECT, Collections.EMPTY_LIST);
        }

        if (processList.size() == 1) {
            Map<String, String> boundaryMap = validateProcessWithOne(processList.get(0));
            sysBoundaryFrom = boundaryMap.get("from");
            sysBoundaryTo = boundaryMap.get("to");
        } else {
            List<Process> root = processRepository.findRootProcess(projectId);
            validateRootProcess(root.get(0));
            String rootStage = root.get(0).getLifeCycleStage().getName();
            if (!toGateList.contains(rootStage)) {
                sysBoundaryTo = Constants.BOUNDARY_GRAVE;
            }
        }

        List<UUID> processIds = processList.stream()
                .map(Process::getId)
                .collect(Collectors.toList());

        List<Exchanges> dataList = exchangesRepository.findAllByElementary(processIds);

        if (dataList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.ELEMENTARY_CANNOT_BE_EMPTY);
        }

        List<Exchanges> allExchanges = exchangesRepository.findAllByProcessIdsInput(processIds);

        // Truy vấn connectors và kiểm tra
        Set<Connector> connectors = connectorRepository.findAllByProcessIds(processIds);
        if (processList.size() > 1 && connectors.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_CONNECTOR_TO_CALCULATE, Collections.EMPTY_LIST);
        }

        List<Process> processesWithoutOutgoingConnectors = processRepository
                .findProcessesWithoutOutgoingConnectors(projectId);
        if (processesWithoutOutgoingConnectors.size() > 1) {
            throw CustomExceptions.badRequest(MessageConstants.FINAL_PROCESS_MUST_BE_SPECIFIED, Collections.EMPTY_LIST);
        }
        if (processIds.size() > 1) {
            boolean allValuesZero = allExchanges.stream()
                    .allMatch(exchange -> exchange.getValue().compareTo(BigDecimal.ZERO) == 0);
            if (allValuesZero) {
                throw CustomExceptions.badRequest(MessageConstants.ALL_EXCHANGE_VALUE_ZERO, Collections.EMPTY_LIST);
            }
            findWay(connectors);
        }
        ProcessNodeDto dto = calculationFast(projectId);
        updateProjectValue(processIds, projectId);
        SystemBoundary systemBoundary = systemBoundaryRepository.findByFromAndTo(sysBoundaryFrom, sysBoundaryTo).orElse(null);
        project.setSystemBoundary(systemBoundary);
        projectRepository.save(project);
        return dto;
    }

    private void validateRootProcess(Process process) {
        List<Exchanges> elementary = exchangesRepository.findElementaryByProcess(process.getId());

        Exchanges productOut = exchangesRepository.findProductOutWithOneProcess(process.getId());
        if (productOut == null) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION +
                    "- Process output is empty.", Collections.EMPTY_LIST);
        }

        if (productOut.getValue().compareTo(BigDecimal.ZERO) == 0) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION +
                    "- Process " + process.getName() + " should have 0 output.", Collections.EMPTY_LIST);
        }
    }

    private Map<String, String> validateProcessWithOne(Process p) {
        Map<String, String> boundaryMap = new HashMap<>();
        List<Exchanges> elementary = exchangesRepository.findElementaryByProcess(p.getId());
        if (elementary.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION +
                    "- Process " + p.getName() + " has no elementary exchange.", Collections.EMPTY_LIST);
        }
        List<Exchanges> productIn = exchangesRepository.findProductIn(p.getId());
        Exchanges productOut = exchangesRepository.findProductOutWithOneProcess(p.getId());
        if (productOut == null) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION +
                    "- Process output is empty.", Collections.EMPTY_LIST);
        }
        if (productOut.getValue().compareTo(BigDecimal.ZERO) == 0) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION +
                    "- Process " + p.getName() + " should have 0 input/output.", Collections.EMPTY_LIST);
        }
        // only output
        if (productIn.isEmpty()) {
            boundaryMap.put("from", Constants.BOUNDARY_CRADLE);
            boundaryMap.put("to", Constants.BOUNDARY_GATE);
        } else {
            boundaryMap.put("from", Constants.BOUNDARY_GATE);
            boundaryMap.put("to", Constants.BOUNDARY_GATE);
        }
        return boundaryMap;
    }

    private void updateProjectValue(List<UUID> processIds, UUID projectId) {
        Project project = projectRepository.findByIdAndStatusTrue(projectId).orElseThrow();

        List<ImpactMethodCategory> categories = impactMethodCategoryRepository
                .findByMethod(project.getLifeCycleImpactAssessmentMethod().getId());
        List<ProjectImpactValue> existingValues = projectImpactValueRepository.findAllByProjectId(projectId);

        Map<UUID, BigDecimal> processImpactSums = processImpactValueRepository
                .findAllByProcessIds(processIds)
                .stream()
                .collect(Collectors.groupingBy(
                        x -> x.getImpactMethodCategory().getId(),
                        Collectors.mapping(ProcessImpactValue::getSystemLevel,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        if (existingValues.isEmpty()) {
            List<ProjectImpactValue> newValues = categories.stream().map(category -> {
                ProjectImpactValue value = new ProjectImpactValue();
                value.setProject(project);
                value.setImpactMethodCategory(category);
                value.setValue(processImpactSums.getOrDefault(category.getId(), BigDecimal.ZERO));
                return value;
            }).toList();
            projectImpactValueRepository.saveAll(newValues);
        } else {
            existingValues.forEach(value -> {
                BigDecimal sum = processImpactSums.getOrDefault(value.getImpactMethodCategory().getId(),
                        BigDecimal.ZERO);
                value.setValue(sum);
            });
            projectImpactValueRepository.saveAll(existingValues);
        }
    }

    private void findWay(Set<Connector> connectors) {
        BigDecimal totalWay = connectors.stream()
                .filter(Objects::nonNull) // Loại bỏ các `null` Connector
                .map(connector -> {
                    BigDecimal startValue = connector.getStartExchanges().getValue();
                    BigDecimal endValue = connector.getEndExchanges().getValue();

                    // Kiểm tra nếu startValue bằng 0
                    if (startValue.compareTo(BigDecimal.ZERO) == 0) {

                        throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION + "- Process " + connector.getStartProcess().getName() + "'s product values cannot be 0.", Collections.EMPTY_LIST);
                    }

                    // Trả về kết quả chia
                    return endValue.divide(startValue, MathContext.DECIMAL128);
                })
                .reduce(BigDecimal.ONE, BigDecimal::multiply); // Tính tích của tất cả các giá trị chia
    }

    private ProcessNodeDto calculationFast(UUID projectId) {
        ProcessNodeDto dto = processService.constructListProcessNodeDto(projectId);
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


    private static Map<UUID, BigDecimal> aggregateNet(ProcessNodeDto root) {
        Map<UUID, BigDecimal> result = new HashMap<>();
        aggregateNetRecursive(root, result);
        return result;
    }

    private static void aggregateNetRecursive(ProcessNodeDto node, Map<UUID, BigDecimal> result) {
        if (node == null) return;

        // Cộng giá trị `net` của node hiện tại vào `Map`
        result.put(
                node.getProcessId(),
                result.getOrDefault(node.getProcessId(), BigDecimal.ZERO).add(node.getNet())
        );

        // Đệ quy xử lý các `subProcesses`
        for (ProcessNodeDto subProcess : node.getSubProcesses()) {
            aggregateNetRecursive(subProcess, result);
        }
    }

    public List<LifeCycleBreakdownDto> buildLifeCycleBreakdown(UUID projectId) {
        List<LifeCycleStage> lifeCycleStages = lcsRepository.findAll();
        List<LifeCycleBreakdownDto> dto = lifeCycleStages.stream().map(lcsConverter::toPercent).collect(Collectors.toList());

        List<Process> processes = processRepository.findAll(projectId);

        Map<UUID, List<Process>> lifeCycleStageToProcessesMap = new HashMap<>();

        for (Process p : processes) {
            UUID stageId = p.getLifeCycleStage().getId();
            lifeCycleStageToProcessesMap
                    .computeIfAbsent(stageId, k -> new ArrayList<>())
                    .add(p);
        }

        for (LifeCycleBreakdownDto x : dto) {
            List<ProcessLifeCycleBreakdownDto> process = new ArrayList<>();
            List<Process> relatedProcesses = lifeCycleStageToProcessesMap.getOrDefault(x.getId(), Collections.emptyList());

            for (Process p : relatedProcesses) {
                ProcessLifeCycleBreakdownDto data = new ProcessLifeCycleBreakdownDto(p.getId(), p.getOverAllProductFlowRequired());
                process.add(data);
            }
            x.setProcess(process);
        }
        return dto;
    }

    public List<LifeCycleBreakdownPercentDto> buildLifeCycleBreakdownWhenGetAll(UUID projectId) {
        if (projectImpactValueRepository.findAllByProjectId(projectId).isEmpty()) {
            return Collections.emptyList();
        }
        List<LifeCycleStage> lifeCycleStages = lcsRepository.findAll();
        List<ImpactCategory> categories = icRepository.findAllByStatus(true);
        List<LifeCycleBreakdownPercentDto> dto = categories.stream()
                .map(icConverter::fromCategoryToBreakDown)
                .toList();

        List<Process> processes = processRepository.findAll(projectId);
        List<UUID> processIds = processes.stream()
                .map(Process::getId)
                .toList();

        // Load all data for calculations in one go
        Map<UUID, List<ProcessImpactValue>> processImpactValuesByCategory = processImpactValueRepository
                .findAllByProcessIds(processIds)
                .stream()
                .collect(Collectors.groupingBy(piv -> piv.getImpactMethodCategory().getImpactCategory().getId()));


        for (LifeCycleBreakdownPercentDto breakdownDto : dto) {
            UUID categoryId = breakdownDto.getId();
            BigDecimal total = processImpactValuesByCategory.getOrDefault(categoryId, List.of())
                    .stream()
                    .map(piv -> piv.getUnitLevel().multiply(piv.getProcess().getOverAllProductFlowRequired()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<LifeCycleStagePercentDto> lcsDto = lifeCycleStages.stream()
                    .map(lcsConverter::toPercentGetAll)
                    .toList();

            for (LifeCycleStagePercentDto stageDto : lcsDto) {
                UUID stageId = stageDto.getId();
                BigDecimal percent = processImpactValuesByCategory.getOrDefault(categoryId, List.of())
                        .stream()
                        .filter(piv -> piv.getProcess().getLifeCycleStage().getId().equals(stageId))
                        .map(piv -> piv.getUnitLevel().multiply(piv.getProcess().getOverAllProductFlowRequired()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                stageDto.setPercent(total.compareTo(BigDecimal.ZERO) == 0
                        ? BigDecimal.ZERO
                        : percent.divide(total, RoundingMode.HALF_UP));
            }

            breakdownDto.setLifeCycleStage(lcsDto);
        }

        return dto;
    }

}
