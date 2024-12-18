package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ImpactCategoryConverter;
import com.example.cabonerfbe.converter.LifeCycleStageConverter;
import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.services.ProcessImpactValueService;
import com.example.cabonerfbe.services.ProcessService;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class Process impact value service.
 *
 * @author SonPHH.
 */
@Slf4j
@Lazy
@Service
public class ProcessImpactValueServiceImpl implements ProcessImpactValueService {

    private final List<String> toGraveList = Arrays.asList("End-of-life", "Use");
    private final SystemBoundaryRepository systemBoundaryRepository;
    private final ProcessRepository processRepository;
    private final ImpactMethodCategoryRepository impactMethodCategoryRepository;
    private final ProcessImpactValueRepository processImpactValueRepository;
    private final ExchangesRepository exchangesRepository;
    private final MidpointImpactCharacterizationFactorsRepository midpointFactorsRepository;
    private final UnitServiceImpl unitService;
    private final ProjectRepository projectRepository;
    private final ConnectorRepository connectorRepository;
    private final ProjectImpactValueRepository projectImpactValueRepository;
    private final ProcessService processService;
    private final LifeCycleStageRepository lcsRepository;
    private final LifeCycleStageConverter lcsConverter;
    private final ImpactCategoryRepository icRepository;
    private final ImpactCategoryConverter icConverter;

    /**
     * Instantiates a new Process impact value service.
     *
     * @param impactMethodCategoryRepository the impact method category repository
     * @param processRepository              the process repository
     * @param systemBoundaryRepository       the system boundary repository
     * @param processImpactValueRepository   the process impact value repository
     * @param exchangesRepository            the exchanges repository
     * @param midpointFactorsRepository      the midpoint factors repository
     * @param unitService                    the unit service
     * @param icRepository                   the ic repository
     * @param icConverter                    the ic converter
     * @param projectRepository              the project repository
     * @param connectorRepository            the connector repository
     * @param projectImpactValueRepository   the project impact value repository
     * @param processService                 the process service
     * @param lcsConverter                   the lcs converter
     * @param lcsRepository                  the lcs repository
     */
    @Autowired
    public ProcessImpactValueServiceImpl(ImpactMethodCategoryRepository impactMethodCategoryRepository,
                                         ProcessRepository processRepository, SystemBoundaryRepository systemBoundaryRepository,
                                         ProcessImpactValueRepository processImpactValueRepository, ExchangesRepository exchangesRepository, MidpointImpactCharacterizationFactorsRepository midpointFactorsRepository, UnitServiceImpl unitService, ImpactCategoryRepository icRepository, ImpactCategoryConverter icConverter, ProjectRepository projectRepository, ConnectorRepository connectorRepository, ProjectImpactValueRepository projectImpactValueRepository, ProcessService processService, LifeCycleStageConverter lcsConverter, LifeCycleStageRepository lcsRepository) {
        this.systemBoundaryRepository = systemBoundaryRepository;
        this.impactMethodCategoryRepository = impactMethodCategoryRepository;
        this.processRepository = processRepository;
        this.processImpactValueRepository = processImpactValueRepository;
        this.exchangesRepository = exchangesRepository;
        this.midpointFactorsRepository = midpointFactorsRepository;
        this.unitService = unitService;
        this.icRepository = icRepository;
        this.icConverter = icConverter;
        this.projectRepository = projectRepository;
        this.connectorRepository = connectorRepository;
        this.projectImpactValueRepository = projectImpactValueRepository;
        this.processService = processService;
        this.lcsConverter = lcsConverter;
        this.lcsRepository = lcsRepository;
    }
    // this one will be used once the client is too tired to update one by one.
    // however this still needs to be optimized

    /**
     * Compute process impact value all exchange of process method.
     *
     * @param process the process
     */
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

    /**
     * Compute process impact value single exchange method.
     *
     * @param process      the process
     * @param exchange     the exchange
     * @param initialValue the initial value
     */
    @Transactional
    public void computeProcessImpactValueSingleExchange(Process process, Exchanges exchange, BigDecimal initialValue) {
        UUID processId = process.getId();
        log.info("Starting impact value computation for process ID: " + processId);

        UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
        Unit baseUnit = exchange.getEmissionSubstance().getUnit();

        Optional<Exchanges> exchanges = exchangesRepository.findProductOut(processId);
        BigDecimal totalFlow = exchanges.map(value -> process.getOverAllProductFlowRequired()).orElse(BigDecimal.ONE);

        List<MidpointImpactCharacterizationFactors> factorsList = midpointFactorsRepository.findByEmissionSubstanceId(emissionSubstanceId);
        int index = 0;

        for (MidpointImpactCharacterizationFactors factor : factorsList) {
            boolean success = false;
            int retryCount = 0;
            int maxRetries = 5;

            while (!success && retryCount < maxRetries) {
                try {
                    Optional<ProcessImpactValue> processImpactValueOpt = processImpactValueRepository.findByProcessIdAndImpactMethodCategoryId(
                            processId, factor.getImpactMethodCategory().getId());

                    if (processImpactValueOpt.isPresent()) {
                        log.info("Start processing index: " + index++);
                        ProcessImpactValue processImpactValue = processImpactValueOpt.get();

                        BigDecimal unitLevel = processImpactValue.getUnitLevel();
                        BigDecimal systemLevel = processImpactValue.getSystemLevel();

                        log.info("Exchange name: " + exchange.getName());
                        log.info("Processing impact category name: " + processImpactValue.getImpactMethodCategory().getImpactCategory().getName());
                        log.info("Initial unit level: " + unitLevel + ", scale: " + unitLevel.scale());
                        log.info("Base exchange value (before converted): " + exchange.getValue() + ", scale: " + exchange.getValue().scale());
                        log.info("Initial value: " + initialValue + ", scale: " + initialValue.scale());

                        // Calculate exchange value and convert to base unit if needed
                        BigDecimal exchangeValue = exchange.getValue().subtract(initialValue);
                        if (!baseUnit.getId().equals(exchange.getUnit().getId())) {
                            exchangeValue = unitService.convertValue(exchange.getUnit(), exchangeValue, baseUnit);
                        }

                        log.info("After conversion, exchange value: " + exchangeValue);
                        BigDecimal factorValue = factor.getDecimalValue();
                        log.info("Factor value: " + factorValue + ", scale: " + factorValue.scale());

                        // Update unit level and system level
                        unitLevel = unitLevel.add(exchangeValue.multiply(factorValue));
                        systemLevel = systemLevel.add(exchangeValue.multiply(factorValue.multiply(totalFlow)));

                        log.info("Updated unit level: " + unitLevel + ", scale: " + unitLevel.scale());

                        processImpactValue.setUnitLevel(unitLevel);
                        processImpactValue.setSystemLevel(systemLevel);

                        // Save entity with optimistic locking (version check happens here)
                        processImpactValueRepository.save(processImpactValue);
                    }

                    success = true; // Mark successful after saving
                } catch (OptimisticLockException e) {
                    retryCount++;
                    log.warn("Optimistic lock exception encountered. Retrying... Attempt: " + retryCount);
                    if (retryCount >= maxRetries) {
                        throw CustomExceptions.badRequest("Failed to update ProcessImpactValue due to concurrent updates");
                    }
                }
            }
        }

        // No need for batch save since entities are saved individually with optimistic locking
    }


    /**
     * Compute process impact value of project method.
     *
     * @param project the project
     */
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
        long startTime = System.currentTimeMillis();
        for (Process process : processList) {
            // set process method to new one
            process.setMethodId(methodId);
            computeProcessImpactValueAllExchangeOfProcess(process);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("tính lại unit level nè: " + (endTime - startTime));

        processRepository.saveAll(processList);
    }

    /**
     * Alter prev impact value list method.
     *
     * @param processes the processes
     * @param methodId  the method id
     */
    public void alterPrevImpactValueList(List<Process> processes, UUID methodId) {
        List<ImpactMethodCategory> methodCategories = impactMethodCategoryRepository.findByMethod(methodId);

        Map<UUID, List<ProcessImpactValue>> groupedValues = processImpactValueRepository
                .findAllByProcessIds(processes.stream().map(Process::getId).toList())
                .stream()
                .collect(Collectors.groupingBy(ProcessImpactValue::getProcessId));
        List<ProcessImpactValue> valuesToSave = new ArrayList<>();
        List<ProcessImpactValue> valuesToDelete = new ArrayList<>();

        for (Process process : processes) {
            List<ProcessImpactValue> existingValues = groupedValues.getOrDefault(process.getId(), new ArrayList<>());

            for (int i = 0; i < methodCategories.size(); i++) {
                if (i < existingValues.size()) {
                    ProcessImpactValue value = existingValues.get(i);
                    value.setImpactMethodCategory(methodCategories.get(i));
                    value.setUnitLevel(BigDecimal.ZERO);
                    valuesToSave.add(value);
                } else {
                    valuesToSave.add(processService.createNewProcessImpactValue(process, methodCategories.get(i)));
                }
            }

            if (existingValues.size() > methodCategories.size()) {
                valuesToDelete.addAll(existingValues.subList(methodCategories.size(), existingValues.size()));
            }
        }

        if (!valuesToDelete.isEmpty()) {
            processImpactValueRepository.deleteAll(valuesToDelete);

        }
        if (!valuesToSave.isEmpty()) {
            processImpactValueRepository.saveAll(valuesToSave);
        }

    }


    public ProcessNodeDto calculateProjectImpactValue(UUID projectId) {
        ProcessNodeDto result = computeSystemLevelOfProject(projectId);
//        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        updateProjectValue(projectId);
        return result;
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
            if (root.isEmpty()) {
                throw CustomExceptions.badRequest(MessageConstants.NO_CONNECTOR_TO_CALCULATE);
            }
            if (root.size() > 1) {
                throw CustomExceptions.badRequest(MessageConstants.MUST_BE_ONLY_ONE_FINAL_PROCESS);
            }
            validateRootProcess(root.get(0));
            String rootStage = root.get(0).getLifeCycleStage().getName();
            if (toGraveList.contains(rootStage)) {
                sysBoundaryTo = Constants.BOUNDARY_GRAVE;
            }
        }

        List<UUID> processIds = processList.stream()
                .map(Process::getId)
                .collect(Collectors.toList());

//        if(!checkObjectLibrary(project)){
//            throw CustomExceptions.badRequest(MessageConstants.PROCESS_NOT_SAME_METHOD_WITH_PROJECT);
//        }


        if (!exchangesRepository.existAllByElementary(processIds)) {
            throw CustomExceptions.badRequest(MessageConstants.ELEMENTARY_CANNOT_BE_EMPTY);
        }

        List<Exchanges> allExchanges = exchangesRepository.findAllByProcessIdsInput(processIds);

        // Truy vấn connectors và kiểm tra
        Set<Connector> connectors = connectorRepository.findAllByProcessIds(processIds);
        if (processList.size() > 1 && connectors.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_CONNECTOR_TO_CALCULATE, Collections.EMPTY_LIST);
        }

        if (processRepository.countProcessesWithoutOutgoingConnectors(projectId) > 1) {
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
        SystemBoundary systemBoundary = systemBoundaryRepository.findByFromAndTo(sysBoundaryFrom, sysBoundaryTo).orElse(null);
        project.setSystemBoundary(systemBoundary);
        projectRepository.save(project);
        return processService.calculationFast(projectId);
    }

    private void validateRootProcess(Process process) {
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

    private void updateProjectValue(UUID projectId) {
        List<Process> processList = processRepository.findAll(projectId);
        List<UUID> processIds = processList.stream()
                .map(Process::getId)
                .collect(Collectors.toList());

        Project project = projectRepository.findByIdAndStatusTrue(projectId).orElseThrow();

        List<ImpactMethodCategory> categories = impactMethodCategoryRepository
                .findByMethod(project.getLifeCycleImpactAssessmentMethod().getId());
        List<ProjectImpactValue> existingValues = projectImpactValueRepository.findAllByProjectId(projectId);

        Map<UUID, BigDecimal> processImpactSums = processImpactValueRepository
                .findAllByProcessIds(processIds)
                .stream()
                .collect(Collectors.groupingBy(
                        x -> x.getImpactMethodCategory().getImpactCategory().getId(),
                        Collectors.mapping(ProcessImpactValue::getSystemLevel,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        if (existingValues.isEmpty()) {
            List<ProjectImpactValue> newValues = categories.stream().map(category -> {
                ProjectImpactValue value = new ProjectImpactValue();
                value.setProject(project);
                value.setImpactMethodCategory(category);
                value.setValue(processImpactSums.getOrDefault(category.getImpactCategory().getId(), BigDecimal.ZERO));
                return value;
            }).toList();
            projectImpactValueRepository.saveAll(newValues);
        } else {
            existingValues.forEach(value -> {
                BigDecimal sum = processImpactSums.getOrDefault(
                        value.getImpactMethodCategory().getImpactCategory().getId(),
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

    private boolean checkObjectLibrary(Project project) {

        List<UUID> processIds = processRepository.findAllObjectLibrary(project.getId())
                .stream()
                .map(Process::getId)
                .toList();

        if (processIds.isEmpty()) {
            return true;
        }

        Map<UUID, UUID> methodIdMap = processImpactValueRepository.findMethodIdsForProcesses(processIds);

        UUID projectMethodId = project.getLifeCycleImpactAssessmentMethod().getId();
        return methodIdMap.values().stream().allMatch(methodId -> methodId.equals(projectMethodId));
    }

    /**
     * Compute process impact value of project when change method method.
     *
     * @param project the project
     */
    public void computeProcessImpactValueOfProjectWhenChangeMethod(Project project) {
        UUID projectId = project.getId();
        UUID methodId = project.getLifeCycleImpactAssessmentMethod().getId();
        List<Process> processList = processRepository.findAll(projectId);

        // Lấy tất cả exchanges cùng với factors trong một lần
        Map<UUID, List<MidpointImpactCharacterizationFactors>> factorsCache = new HashMap<>();
        List<Exchanges> allExchanges = exchangesRepository.findAllByProcessIdsAndExchangesType(
                processList.stream().map(Process::getId).collect(Collectors.toList()),
                Constants.ELEMENTARY_EXCHANGE);

        // Tạo danh sách processImpactValues để lưu sau
        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        for (Process process : processList) {
            process.setMethodId(methodId);

            // Lọc exchanges cho process hiện tại
            List<Exchanges> exchangeList = allExchanges.stream()
                    .filter(exchange -> exchange.getProcess().getId().equals(process.getId()))
                    .collect(Collectors.toList());

            // Cập nhật các impact values
            for (Exchanges exchange : exchangeList) {
                UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
                List<MidpointImpactCharacterizationFactors> factorsList = factorsCache.computeIfAbsent(
                        emissionSubstanceId,
                        id -> midpointFactorsRepository.findByEmissionSubstanceId(id)
                );

                BigDecimal exchangeValue = exchange.getValue();
                Unit exchangeUnit = exchange.getUnit();
                Unit baseUnit = exchange.getEmissionSubstance().getUnit();

                for (MidpointImpactCharacterizationFactors factors : factorsList) {
                    Optional<ProcessImpactValue> processImpactValueOpt = processImpactValueRepository
                            .findByProcessIdAndImpactMethodCategoryId(
                                    process.getId(), factors.getImpactMethodCategory().getId());

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
        }

        long endTime = System.currentTimeMillis();
        System.out.println("tính lại unit level nè: " + (endTime - startTime));

        // Lưu tất cả ProcessImpactValue một lần
        long startSaveTime = System.currentTimeMillis();
        processImpactValueRepository.saveAll(processImpactValueList);
        long endSaveTime = System.currentTimeMillis();
        System.out.println("save tính lại unit level nè: " + (endSaveTime - startSaveTime));


    }


    /**
     * Build life cycle breakdown method.
     *
     * @param projectId the project id
     * @return the list
     */
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

    /**
     * Build life cycle breakdown when get all method.
     *
     * @param projectId the project id
     * @return the list
     */
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
