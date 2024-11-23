package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.converter.ConnectorConverter;
import com.example.cabonerfbe.dto.ConnectorPercentDto;
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
    private ConnectorConverter connectorConverter;
    @Autowired
    private ProjectImpactValueRepository projectImpactValueRepository;
    private String exchangeIdNext = null;

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

        List<MidpointImpactCharacterizationFactors> list = midpointFactorsRepository
                .findByEmissionSubstanceId(emissionSubstanceId);

        for (MidpointImpactCharacterizationFactors factors : list) {
            Optional<ProcessImpactValue> processImpactValueOpt = processImpactValueRepository
                    .findByProcessIdAndImpactMethodCategoryId(
                            processId, factors.getImpactMethodCategory().getId());

            if (processImpactValueOpt.isPresent()) {
                ProcessImpactValue processImpactValue = processImpactValueOpt.get();
                BigDecimal unitLevel = processImpactValue.getUnitLevel();

                System.out
                        .println("Processing impact method category ID: " + factors.getImpactMethodCategory().getId());
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

                System.out.println("Factor value: " + factorValue);
                System.out.println("Updated unit level: " + unitLevel);

                processImpactValue.setUnitLevel(unitLevel);
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
        // so we have to use nested loops,
        // forE: {
        // forE: {
        // (and even)
        // forE: {}
        // }
        // }
        // assume that the project is not null
        UUID projectId = project.getId();
        UUID methodId = project.getLifeCycleImpactAssessmentMethod().getId();
        List<Process> processList = processRepository.findAll(projectId);
        for (Process process : processList) {
            // alter the old ones.
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

    public void computeSystemLevelOfProject(UUID projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST));

        // Lấy toàn bộ process và kiểm tra
        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_IN_PROJECT, Collections.EMPTY_LIST);
        }

        List<UUID> processIds = processList.stream()
                .map(Process::getId)
                .collect(Collectors.toList());

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
        computeSystemLevelOfProjectBackground(projectId);
//        updateProcessWhenCalculation(processIds);
        updateProjectValue(processIds, projectId);
    }

    public void computeSystemLevelOfProjectBackground(UUID projectId) {
        connectorsResponse.clear();
        _connectors.clear();

        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        if (processList.isEmpty()) {
            return;
        }
        if (processList.size() == 1) {
            validateProcessWithOne(processList.get(0));
        }

        List<UUID> processIds = processList.stream()
                .map(Process::getId)
                .collect(Collectors.toList());

        // Truy vấn connectors và kiểm tra
        Set<Connector> connectors = connectorRepository.findAllByProcessIds(processIds);
        if (processList.size() > 1 && connectors.isEmpty()) {
            return;
        }
        _connectors.addAll(connectors);

        List<Process> processesWithoutOutgoingConnectors = processRepository
                .findProcessesWithoutOutgoingConnectors(projectId);
        if (processesWithoutOutgoingConnectors.size() > 1) {
            return;
        }

        processImpactValueRepository.setDefaultPrevious(processIds);

        Map<UUID, BigDecimal> processFlowMap = new HashMap<>();
        List<ProcessImpactValue> allImpactValues = new ArrayList<>();

        // Duyệt từng process để tính toán
        for (Process process : processList) {
            UUID processId = process.getId();
            exchangeIdNext = null;
            BigDecimal totalFlow = traversePath(processId, true).setScale(2, RoundingMode.CEILING);
            process.setOverAllProductFlowRequired(totalFlow);

            List<ProcessImpactValue> impactValues = processImpactValueRepository.findByProcessId(processId);
            if (!impactValues.isEmpty()) {
                updateProcess(impactValues, totalFlow, processId);
                allImpactValues.addAll(impactValues);
            }
            processFlowMap.put(processId, totalFlow);
        }

        processRepository.saveAll(processList);
        processImpactValueRepository.saveAll(allImpactValues);

//        updatePreviousProcess();
        return;
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

    // Phương thức đệ quy để duyệt đường đi từ một process và tính toán kết quả cho
    // mỗi nhánh
    private BigDecimal traversePath(UUID processId, boolean isFirstProcess) {
        BigDecimal multiplyNumerator = BigDecimal.ONE;
        BigDecimal multiplyDenominator = BigDecimal.ONE;

        if (isFirstProcess) {
            List<Exchanges> exchanges = exchangesRepository.findProductByProcessId(processId).stream()
                    .filter(exchange -> {
                        if (exchangeIdNext != null) {
                            return !exchange.isInput() || exchange.getId().equals(UUID.fromString(exchangeIdNext));
                        }
                        return !exchange.isInput();
                    })
                    .collect(Collectors.toList());
        } else {
            List<Exchanges> exchanges = exchangesRepository.findProductByProcessId(processId).stream()
                    .filter(exchange -> {
                        if (exchangeIdNext != null) {
                            return !exchange.isInput() || exchange.getId().equals(UUID.fromString(exchangeIdNext));
                        }
                        return !exchange.isInput();
                    })
                    .collect(Collectors.toList());

            for (Exchanges exchange : exchanges) {
                if (exchange.getValue().equals(BigDecimal.ZERO)) {
                    return null;
                }
                BigDecimal conversionFactor = exchange.getUnit().getConversionFactor();
                BigDecimal convertUnit = conversionFactor.compareTo(BigDecimal.ONE) == 0
                        ? exchange.getValue()
                        : exchange.getValue().divide(conversionFactor, MathContext.DECIMAL128);
                if (!exchange.isInput()) {
                    multiplyDenominator = multiplyDenominator.multiply(convertUnit);
                } else {
                    multiplyNumerator = multiplyNumerator.multiply(convertUnit);
                }
            }
        }

        // Lấy tất cả các connector tiếp theo và thực hiện đệ quy cho từng nhánh
        List<Connector> nextConnectors = connectorRepository.findNextByStartProcessId(processId);

        // Nếu không có connector tiếp theo, trả về kết quả của đường đi hiện tại
        if (nextConnectors.isEmpty()) {
            return multiplyNumerator.divide(multiplyDenominator, MathContext.DECIMAL128);
        }

        // Nếu có connector tiếp theo, tiếp tục duyệt các đường đi khác và cộng dồn kết
        // quả
        BigDecimal pathTotal = BigDecimal.ZERO;
        for (Connector nextConnector : nextConnectors) {
            UUID nextProcessId = nextConnector.getEndProcess().getId();
            exchangeIdNext = nextConnector.getEndExchanges().getId().toString();
            pathTotal = pathTotal.add(traversePath(nextProcessId, false));
        }

        // Kết hợp kết quả hiện tại với kết quả của các đường đi con
        return pathTotal.multiply(multiplyNumerator.divide(multiplyDenominator, MathContext.DECIMAL128));
    }

    private void updateProcess(List<ProcessImpactValue> list, BigDecimal totalRequiredFlow, UUID currentProcessId) {
        Exchanges o = exchangesRepository.findProductOut(currentProcessId)
                .orElse(null);
        BigDecimal outputValue = exchangesRepository.findProductOut(currentProcessId)
                .map(Exchanges::getValue)
                .orElse(BigDecimal.ONE);

        list.forEach(x -> {
            BigDecimal value = totalRequiredFlow.equals(BigDecimal.ZERO)
                    ? x.getUnitLevel()
                    : totalRequiredFlow.multiply(x.getUnitLevel())
                    .divide(outputValue, MathContext.DECIMAL128);

            x.setSystemLevel(value);
//            x.setOverallImpactContribution(value);
        });

        processImpactValueRepository.saveAll(list);
    }

    private void updateProcessWhenCalculation(List<UUID> processIds) {
        List<ProcessImpactValue> value = processImpactValueRepository.findAllByProcessIds(processIds);
        for (ProcessImpactValue x : value) {
            x.setSystemLevel(x.getOverallImpactContribution());
        }
        processImpactValueRepository.saveAll(value);
    }

    private void updateProjectValue(List<UUID> processIds, UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();

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


    private void updatePreviousProcess() {
        List<ProcessImpactValue> updatedValues = _connectors.stream()
                .flatMap(connector -> {
                    Exchanges startExchange = exchangesRepository.findById(connector.getStartExchanges().getId())
                            .orElseThrow();
                    Exchanges endExchange = exchangesRepository.findById(connector.getEndExchanges().getId())
                            .orElseThrow();

                    BigDecimal divisor = endExchange.getValue().divide(startExchange.getValue(),
                            MathContext.DECIMAL128);

                    List<ProcessImpactValue> startValues = processImpactValueRepository
                            .findAllByProcess(connector.getStartProcess());
                    List<ProcessImpactValue> endValues = processImpactValueRepository
                            .findAllByProcess(connector.getEndProcess());

                    return startValues.stream().flatMap(start -> endValues.stream().map(end -> {
                        if (start.getImpactMethodCategory().equals(end.getImpactMethodCategory())) {
                            end.setPreviousProcessValue(
                                    end.getPreviousProcessValue().add(divisor.multiply(start.getUnitLevel())));
                        }
                        return end;
                    }));
                })
                .toList();

        processImpactValueRepository.saveAll(updatedValues);
    }

    // private void connectorCalculation(UUID projectId) {
    // List<ProjectImpactValue> projectValues =
    // projectImpactValueRepository.findAllByProjectId(projectId);
    //
    // _connectors.forEach(connector -> {
    // BigDecimal divisor = findWay(connector);
    //
    // List<ProcessImpactValue> startValues =
    // processImpactValueRepository.findAllByProcess(connector.getStartProcess());
    // ConnectorPercentDto dto =
    // connectorConverter.fromConnectorToConnectorPercentDto(connector);
    //
    // projectValues.stream()
    // .flatMap(projectValue -> startValues.stream()
    // .filter(start ->
    // start.getImpactMethodCategory().equals(projectValue.getImpactMethodCategory())
    // && projectValue.getValue().compareTo(BigDecimal.ZERO) > 0)
    // .map(start -> {
    // BigDecimal totalValue =
    // start.getPreviousProcessValue().add(start.getUnitLevel()).multiply(divisor);
    // dto.setPercent(totalValue.divide(projectValue.getValue(), 2,
    // RoundingMode.HALF_UP));
    // return dto;
    // }))
    // .findFirst()
    // .ifPresent(connectorsResponse::add);
    // });
    // }

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

        // In kết quả cuối cùng
    }

}
