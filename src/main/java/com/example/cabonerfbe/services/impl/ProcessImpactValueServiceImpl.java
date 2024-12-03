package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ConnectorPercentDto;
import com.example.cabonerfbe.dto.ProcessNodeDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProcessImpactValueRequest;
import com.example.cabonerfbe.services.ProcessImpactValueService;
import com.example.cabonerfbe.services.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessImpactValueServiceImpl implements ProcessImpactValueService {
    private final List<ConnectorPercentDto> connectorsResponse = new ArrayList<>();
    private final ProjectImpactValue totalProject = new ProjectImpactValue();
    private final List<Connector> _connectors = new ArrayList<>();
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
    private ProcessService processService;

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
        long startTime = System.currentTimeMillis();
        for (Process process : processList) {
            // set process method to new one
            process.setMethodId(methodId);
            computeProcessImpactValueAllExchangeOfProcess(process);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("tính lại unit level nè: "+ (endTime - startTime));

        processRepository.saveAll(processList);
    }

    public void alterPrevImpactValueList(List<Process> processes, UUID methodId) {
        long startTime = System.currentTimeMillis();
        List<ImpactMethodCategory> methodCategories = impactMethodCategoryRepository.findByMethod(methodId);

        long endTime = System.currentTimeMillis();
        System.out.println("lấy method từ db nè: "+ (endTime - startTime));
        long startTimeProcess = System.currentTimeMillis();

        Map<UUID, List<ProcessImpactValue>> groupedValues = processImpactValueRepository
                .findAllByProcessIds(processes.stream().map(Process::getId).toList())
                .stream()
                .collect(Collectors.groupingBy(ProcessImpactValue::getProcessId));
        long endTimeProcess = System.currentTimeMillis();
        System.out.println("lấy process impact value từ db nè: "+ (endTimeProcess - startTimeProcess));
        List<ProcessImpactValue> valuesToSave = new ArrayList<>();
        List<ProcessImpactValue> valuesToDelete = new ArrayList<>();
//
        long startTimeForProcess = System.currentTimeMillis();

        for (Process process : processes) {
            List<ProcessImpactValue> existingValues = groupedValues.getOrDefault(process.getId(), new ArrayList<>());

            for (int i = 0; i < methodCategories.size(); i++) {
                if (i < existingValues.size()) {
                    ProcessImpactValue value = existingValues.get(i);
                    value.setImpactMethodCategory(methodCategories.get(i));
                    value.setUnitLevel(BigDecimal.ZERO);
                    valuesToSave.add(value);
                } else {
                    valuesToSave.add(getNewProcessImpactValue(methodCategories.get(i), process));
                }
            }

            if (existingValues.size() > methodCategories.size()) {
                valuesToDelete.addAll(existingValues.subList(methodCategories.size(), existingValues.size()));
            }
        }


        long endTimeForProcess = System.currentTimeMillis();
        System.out.println("lấy process impact value từ db nè: "+ (endTimeForProcess - startTimeForProcess));

        if(!valuesToDelete.isEmpty()){
            processImpactValueRepository.deleteAll(valuesToDelete);

        }
        if(!valuesToSave.isEmpty()){
            long startTimeSave = System.currentTimeMillis();
            processImpactValueRepository.saveAll(valuesToSave);

            long endTimeSave = System.currentTimeMillis();
            System.out.println("save nè: "+ (endTimeSave - startTimeSave));
        }

    }



    public ProcessNodeDto calculateProjectImpactValue(UUID projectId) {
        ProcessNodeDto result = computeSystemLevelOfProject(projectId);
        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        updateProjectValue(processList, projectId);
        return result;
    }

    public ProcessNodeDto computeSystemLevelOfProject(UUID projectId) {

        Project project = projectRepository.findByIdAndStatusTrue(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST));

        // Lấy toàn bộ process và kiểm tra
        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_IN_PROJECT, Collections.EMPTY_LIST);
        }
        if (processList.size() == 1) {
            validateProcessWithOne(processList.get(0));
        }
        List<UUID> processIds = processList.stream()
                .map(Process::getId)
                .collect(Collectors.toList());

//        if(!checkObjectLibrary(project)){
//            throw CustomExceptions.badRequest(MessageConstants.PROCESS_NOT_SAME_METHOD_WITH_PROJECT);
//        }

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
        return processService.calculationFast(projectId);
    }

    private void validateProcessWithOne(Process p) {
        List<Exchanges> elementary = exchangesRepository.findElementaryByProcess(p.getId());
        if (elementary.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION +
                    "- Process " + p.getName() + " has no elementary exchange.", Collections.EMPTY_LIST);
        }
        List<Exchanges> productIn = exchangesRepository.findProductIn(p.getId());
        Exchanges productOut = exchangesRepository.findProductOutWithOneProcess(p.getId());
        if (!productIn.isEmpty() || productOut != null) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_PERFORM_CALCULATION +
                    "- Process " + p.getName() + " should have 0 input/output.", Collections.EMPTY_LIST);
        }
    }

    private void updateProjectValue(List<Process> processList, UUID projectId) {

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


}
