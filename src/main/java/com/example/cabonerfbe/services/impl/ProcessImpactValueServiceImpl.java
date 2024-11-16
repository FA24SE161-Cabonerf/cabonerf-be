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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final List<ConnectorPercentDto> connectorsResponse = new ArrayList<>();
    private final ProjectImpactValue totalProject = new ProjectImpactValue();
    private final List<Connector> _connectors = new ArrayList<>();
    private Process lastProcess = new Process();

    @RabbitListener(queues = RabbitMQConfig.CREATE_PROCESS_QUEUE)
    private void processImpactValueGenerate(CreateProcessImpactValueRequest request) {
        UUID processId = request.getProcessId();
        UUID methodId = request.getMethodId();
        Process process = processRepository.findByProcessId(processId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROCESS_FOUND)
        );
        List<ImpactMethodCategory> methodCategoryList = impactMethodCategoryRepository.findByMethod(methodId);
        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();
        for (ImpactMethodCategory methodCategory : methodCategoryList) {
            ProcessImpactValue processImpactValue = getNewProcessImpactValue(methodCategory, process);
            processImpactValueList.add(processImpactValue);
        }
        processImpactValueRepository.saveAll(processImpactValueList);
    }

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

    // this one will be used once the client is too tired to update one by one.
    // however this still needs to be optimized
    public void computeProcessImpactValueAllExchange(Process process) {
        UUID methodId = process.getProject().getLifeCycleImpactAssessmentMethod().getId();
        UUID processId = process.getId();

        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();

        List<Exchanges> exchangeList = exchangesRepository.findAllByProcessIdAndExchangesType(processId, Constants.ELEMENTARY_EXCHANGE);
        List<ImpactMethodCategory> impactMethodCategoryList = impactMethodCategoryRepository.findByMethod(methodId);

        for (ImpactMethodCategory methodCategory : impactMethodCategoryList) {
            UUID methodCategoryId = methodCategory.getId();

            ProcessImpactValue processImpactValue = processImpactValueRepository.findByProcessIdAndImpactMethodCategoryId(processId, methodCategoryId)
                    .orElse(new ProcessImpactValue());

            processImpactValue.setImpactMethodCategory(methodCategory);
            processImpactValue.setProcess(process);
            processImpactValue.setOverallImpactContribution(Constants.NEW_OVERALL_FLOW);
            processImpactValue.setPreviousProcessValue(Constants.DEFAULT_PREVIOUS_PROCESS_VALUE);
            processImpactValue.setSystemLevel(Constants.DEFAULT_SYSTEM_LEVEL);

            BigDecimal unitLevel = Constants.BASE_UNIT_LEVEL;

            for (Exchanges exchange : exchangeList) {
                UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
                BigDecimal exchangeValue = exchange.getValue();

                Optional<MidpointImpactCharacterizationFactors> midpointFactorsOptional =
                        midpointFactorsRepository.findByMethodCategoryAndEmissionSubstance(methodCategoryId, emissionSubstanceId);

                // Calculate and add to unit level based on presence of midpoint factors
                unitLevel = unitLevel.add(midpointFactorsOptional
                        .map(midpointFactors -> exchangeValue.multiply(midpointFactors.getDecimalValue()))
                        .orElse(Constants.BASE_UNIT_LEVEL));
            }

            processImpactValue.setUnitLevel(unitLevel);
            processImpactValueList.add(processImpactValue);
        }

        processImpactValueRepository.saveAll(processImpactValueList);
    }

    public void computeProcessImpactValueSingleExchange(Process process, Exchanges exchange, BigDecimal initialValue) {
        UUID processId = process.getId();
        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();

        UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
        Unit baseUnit = exchange.getEmissionSubstance().getUnit();

        List<MidpointImpactCharacterizationFactors> list = midpointFactorsRepository.findByEmissionSubstanceId(emissionSubstanceId);

        for (MidpointImpactCharacterizationFactors factors : list) {
            Optional<ProcessImpactValue> processImpactValue = processImpactValueRepository.findByProcessIdAndImpactMethodCategoryId(processId, factors.getImpactMethodCategory().getId());
            if (processImpactValue.isPresent()) {
                BigDecimal unitLevel = processImpactValue.get().getUnitLevel();
                // Convert the exchange value to the base unit and adjust based on initial value
                BigDecimal exchangeValue = unitService.convertValue(
                        exchange.getUnit(),
                        exchange.getValue().subtract(initialValue),
                        baseUnit
                );
                // Adjust unit level by adding the product of exchange value and factor
                BigDecimal factorValue = factors.getDecimalValue();
                unitLevel = unitLevel.add(exchangeValue.multiply(factorValue).setScale(Constants.BIG_DECIMAL_DEFAULT_SCALE, RoundingMode.HALF_UP));

                processImpactValue.get().setUnitLevel(unitLevel);
                processImpactValueList.add(processImpactValue.get());
            }
        }

        // Batch save processImpactValues in chunks
        int batchSize = 100;
        for (int i = 0; i < processImpactValueList.size(); i += batchSize) {
            List<ProcessImpactValue> batch = processImpactValueList.subList(i, Math.min(i + batchSize, processImpactValueList.size()));
            processImpactValueRepository.saveAll(batch);
        }
    }

    public List<ConnectorPercentDto> computeSystemLevelOfProject(UUID projectId) {
        connectorsResponse.clear();
        _connectors.clear();

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND)
        );

        // Lấy toàn bộ process và kiểm tra
        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_IN_PROJECT);
        }

        List<UUID> processIds = processList.stream()
                .map(Process::getId)
                .collect(Collectors.toList());

        // Truy vấn connectors và kiểm tra
        List<Connector> connectors = connectorRepository.findAllByProcessIds(processIds);
        if (processList.size() > 1 && connectors.isEmpty()) {
            throw CustomExceptions.notFound("There must be at least one connector to calculate");
        }
        _connectors.addAll(connectors);

        List<Process> processesWithoutOutgoingConnectors = processRepository.findProcessesWithoutOutgoingConnectors();
        if (processesWithoutOutgoingConnectors.size() > 1) {
            throw CustomExceptions.badRequest("Multiple deepest process found");
        }else{
            lastProcess = processesWithoutOutgoingConnectors.get(0);
        }

        processImpactValueRepository.setDefaultPrevious(processIds);

        Map<UUID, BigDecimal> processFlowMap = new HashMap<>();
        List<ProcessImpactValue> allImpactValues = new ArrayList<>();

        // Xử lý song song để tính toán từng process
        processList.parallelStream().forEach(process -> {
            UUID processId = process.getId();
            BigDecimal totalFlow = traversePath(processId, null, true).setScale(2, RoundingMode.CEILING);
            process.setOverAllProductFlowRequired(totalFlow);

            List<ProcessImpactValue> impactValues = processImpactValueRepository.findByProcessId(processId);
            if (!impactValues.isEmpty()) {
                updateProcess(impactValues, totalFlow, processId);
                synchronized (allImpactValues) {
                    allImpactValues.addAll(impactValues);
                }
            }
            processFlowMap.put(processId, totalFlow);
        });

        processRepository.saveAll(processList);
        processImpactValueRepository.saveAll(allImpactValues);

        updatePreviousProcess();
        updateProjectValue(processIds, projectId);
        calculationConnector(projectId);

        return connectorsResponse;
    }

    // Phương thức đệ quy để duyệt đường đi từ một process và tính toán kết quả cho mỗi nhánh
    private BigDecimal traversePath(UUID processId, String previousExchangeName, boolean isFirstProcess) {
        BigDecimal multiplyNumerator = BigDecimal.ONE;
        BigDecimal multiplyDenominator = BigDecimal.ONE;

        if (isFirstProcess) {
            String finalPreviousExchangeName = previousExchangeName;
            List<Exchanges> exchanges = exchangesRepository.findProductByProcessId(processId).stream()
                    .filter(exchange -> !exchange.isInput() || exchange.getName().equals(finalPreviousExchangeName))
                    .collect(Collectors.toList());

            for (Exchanges exchange : exchanges) {
                if (!exchange.isInput()) {
                    previousExchangeName = exchange.getName();
                }
            }
        } else {
            String finalPreviousExchangeName = previousExchangeName;
            List<Exchanges> exchanges = exchangesRepository.findProductByProcessId(processId).stream()
                    .filter(exchange -> !exchange.isInput() || exchange.getName().equals(finalPreviousExchangeName))
                    .collect(Collectors.toList());

            for (Exchanges exchange : exchanges) {
                BigDecimal conversionFactor = exchange.getUnit().getConversionFactor();
                BigDecimal convertUnit = conversionFactor.compareTo(BigDecimal.ONE) == 0
                        ? exchange.getValue()
                        : exchange.getValue().divide(conversionFactor, MathContext.DECIMAL128);
                if (!exchange.isInput()) {
                    previousExchangeName = exchange.getName();
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

        // Nếu có connector tiếp theo, tiếp tục duyệt các đường đi khác và cộng dồn kết quả
        BigDecimal pathTotal = BigDecimal.ZERO;
        for (Connector nextConnector : nextConnectors) {
            UUID nextProcessId = nextConnector.getEndProcess().getId();
            pathTotal = pathTotal.add(traversePath(nextProcessId, previousExchangeName, false));
        }

        // Kết hợp kết quả hiện tại với kết quả của các đường đi con
        return pathTotal.multiply(multiplyNumerator.divide(multiplyDenominator, MathContext.DECIMAL128));
    }


    private void updateProcess(List<ProcessImpactValue> list, BigDecimal totalRequiredFlow, UUID currentProcessId) {
        BigDecimal outputValue = exchangesRepository.findProductOut(currentProcessId)
                .map(Exchanges::getValue)
                .orElse(BigDecimal.ONE);

        list.forEach(x -> {
            BigDecimal value = totalRequiredFlow.equals(BigDecimal.ZERO)
                    ? x.getUnitLevel()
                    : totalRequiredFlow.multiply(x.getUnitLevel())
                    .divide(outputValue, MathContext.DECIMAL128);

            x.setSystemLevel(value);
            x.setOverallImpactContribution(value);
        });

        processImpactValueRepository.saveAll(list);
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
                        Collectors.mapping(ProcessImpactValue::getSystemLevel, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

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
                BigDecimal sum = processImpactSums.getOrDefault(value.getImpactMethodCategory().getId(), BigDecimal.ZERO);
                value.setValue(sum);
            });
            projectImpactValueRepository.saveAll(existingValues);
        }
    }

    private void updatePreviousProcess() {
        List<ProcessImpactValue> updatedValues = _connectors.stream()
                .flatMap(connector -> {
                    Exchanges startExchange = exchangesRepository.findById(connector.getStartExchanges().getId()).orElseThrow();
                    Exchanges endExchange = exchangesRepository.findById(connector.getEndExchanges().getId()).orElseThrow();

                    BigDecimal divisor = endExchange.getValue().divide(startExchange.getValue(), MathContext.DECIMAL128);

                    List<ProcessImpactValue> startValues = processImpactValueRepository.findAllByProcess(connector.getStartProcess());
                    List<ProcessImpactValue> endValues = processImpactValueRepository.findAllByProcess(connector.getEndProcess());

                    return startValues.stream().flatMap(start -> endValues.stream().map(end -> {
                        if (start.getImpactMethodCategory().equals(end.getImpactMethodCategory())) {
                            end.setPreviousProcessValue(end.getPreviousProcessValue().add(divisor.multiply(start.getUnitLevel())));
                        }
                        return end;
                    }));
                })
                .toList();

        processImpactValueRepository.saveAll(updatedValues);
    }

    private void calculationConnector(UUID projectId) {
        List<ProjectImpactValue> projectValues = projectImpactValueRepository.findAllByProjectId(projectId);

        _connectors.forEach(connector -> {
            BigDecimal divisor = findWay(connector);

            List<ProcessImpactValue> startValues = processImpactValueRepository.findAllByProcess(connector.getStartProcess());
            ConnectorPercentDto dto = connectorConverter.fromConnectorToConnectorPercentDto(connector);

            projectValues.stream()
                    .flatMap(projectValue -> startValues.stream()
                            .filter(start -> start.getImpactMethodCategory().equals(projectValue.getImpactMethodCategory()) && projectValue.getValue().compareTo(BigDecimal.ZERO) > 0)
                            .map(start -> {
                                BigDecimal totalValue = start.getPreviousProcessValue().add(start.getUnitLevel()).multiply(divisor);
                                dto.setPercent(totalValue.divide(projectValue.getValue(), 2, RoundingMode.HALF_UP));
                                return dto;
                            }))
                    .findFirst()
                    .ifPresent(connectorsResponse::add);
        });
    }

    private BigDecimal findWay(Connector connector) {
        BigDecimal totalWay = BigDecimal.ONE;

        while (connector != null) {
            BigDecimal divisor = connector.getEndExchanges().getValue()
                    .divide(connector.getStartExchanges().getValue(), MathContext.DECIMAL128);
            totalWay = totalWay.multiply(divisor);

            connector = connectorRepository.findNextByStartProcessIdOne(connector.getEndProcess().getId());
        }

        return totalWay;
    }

}
