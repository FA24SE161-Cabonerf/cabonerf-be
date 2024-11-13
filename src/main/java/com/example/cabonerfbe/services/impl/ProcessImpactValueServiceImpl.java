package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    private ProjectImpactValueRepository projectImpactValueRepository;

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
        log.info("Starting impact value computation for process ID: " + processId);

        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();
        UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
        Unit baseUnit = exchange.getEmissionSubstance().getUnit();

        List<MidpointImpactCharacterizationFactors> list = midpointFactorsRepository.findByEmissionSubstanceId(emissionSubstanceId);
        log.info("Retrieved " + list.size() + " midpoint factors for emission substance ID: " + emissionSubstanceId);

        for (MidpointImpactCharacterizationFactors factors : list) {
            Optional<ProcessImpactValue> processImpactValueOpt = processImpactValueRepository.findByProcessIdAndImpactMethodCategoryId(
                    processId, factors.getImpactMethodCategory().getId()
            );

            if (processImpactValueOpt.isPresent()) {
                ProcessImpactValue processImpactValue = processImpactValueOpt.get();
                BigDecimal unitLevel = processImpactValue.getUnitLevel();

                log.info("Processing impact method category ID: " + factors.getImpactMethodCategory().getId());
                log.info("Initial unit level: " + unitLevel);

                // Convert the exchange value to the base unit and adjust based on initial value
                BigDecimal exchangeValue = unitService.convertValue(
                        exchange.getUnit(),
                        exchange.getValue().subtract(initialValue),
                        baseUnit
                );
                log.info("Converted exchange value: " + exchangeValue);

                // Adjust unit level by adding the product of exchange value and factor
                BigDecimal factorValue = factors.getDecimalValue();
                unitLevel = unitLevel.add(exchangeValue.multiply(factorValue));

                log.info("Factor value: " + factorValue);
                log.info("Updated unit level: " + unitLevel);

                processImpactValue.setUnitLevel(unitLevel);
                processImpactValueList.add(processImpactValue);
            } else {
                log.warn("No process impact value found for impact method category ID: " + factors.getImpactMethodCategory().getId());
            }
        }

        // Batch save processImpactValues in chunks
        int batchSize = 100;
        for (int i = 0; i < processImpactValueList.size(); i += batchSize) {
            List<ProcessImpactValue> batch = processImpactValueList.subList(i, Math.min(i + batchSize, processImpactValueList.size()));
            processImpactValueRepository.saveAll(batch);
        }
    }

    public void computeProcessImpactValueOfProject(Project project) {

    }


    public void computeSystemLevelOfProject(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND)
        );

        // Lấy tất cả các process trong dự án
        List<Process> processList = processRepository.findAllWithCreatedAsc(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_IN_PROJECT);
        }

        // Lấy danh sách UUID của tất cả các process
        List<UUID> processIds = processList.stream()
                .map(Process::getId)
                .collect(Collectors.toList());

        if(processIds.size() > 1){
            List<Connector> connectors = connectorRepository.findAllByProcessIds(processIds);
            if (connectors.isEmpty()) {
                throw CustomExceptions.notFound("There must be at least one connector to calculate");
            }


        List<Process> checkProcess = processRepository.findProcessesWithoutOutgoingConnectors();
        if (checkProcess.size() > 1) {
            throw CustomExceptions.badRequest("Multiple deepest process found");
        }

            // Khởi tạo map để lưu trữ các giá trị exchange cho từng process
            for (Process currentProcess : processList) {
                UUID currentProcessId = currentProcess.getId();
                BigDecimal totalFlow = traversePath(currentProcessId, null, true); // Đặt flag isFirstProcess là true
                totalFlow = totalFlow.setScale(2, RoundingMode.CEILING);

                currentProcess.setOverAllProductFlowRequired(totalFlow);


                List<ProcessImpactValue> data = processImpactValueRepository.findByProcessId(currentProcessId);
                if(!data.isEmpty()){
                    updateProcess(data,totalFlow,currentProcessId);
                }


                processRepository.save(currentProcess);
            }
        }
        updateProject(processIds,projectId);
    }

    // Phương thức đệ quy để duyệt đường đi từ một process và tính toán kết quả cho mỗi nhánh
    private BigDecimal traversePath(UUID processId, String previousExchangeName, boolean isFirstProcess) {
        BigDecimal multiplyNumerator = BigDecimal.ONE;
        BigDecimal multiplyDenominator = BigDecimal.ONE;

        // Nếu đây không phải là process đầu tiên thì mới tính giá trị exchange
        if (isFirstProcess) {
            String finalPreviousExchangeName = previousExchangeName;
            List<Exchanges> exchanges = exchangesRepository.findProductByProcessId(processId).stream()
                    .filter(exchange -> !exchange.isInput() || exchange.getName().equals(finalPreviousExchangeName))
                    .collect(Collectors.toList());

            for (Exchanges exchange : exchanges) {
                if (!exchange.isInput()) {
                    previousExchangeName = exchange.getName();
                    multiplyDenominator = multiplyDenominator.multiply(BigDecimal.ONE);
                } else {
                    multiplyNumerator = multiplyNumerator.multiply(BigDecimal.ONE);
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
        Optional<Exchanges> e = exchangesRepository.findProductOut(currentProcessId);

        List<ProcessImpactValue> data = e.isPresent() ? list.stream()
                .peek(x -> {
                    x.setSystemLevel(totalRequiredFlow.multiply(x.getUnitLevel()).divide(e.get().getValue()));
                    x.setOverallImpactContribution(totalRequiredFlow.multiply(x.getUnitLevel()).divide(e.get().getValue()));
                })
                .collect(Collectors.toList())
                : null;
        assert data != null;
        processImpactValueRepository.saveAll(data);
    }

    private void updateProject(List<UUID> processIds, UUID projectId) {
        List<ProjectImpactValue> projectData = projectImpactValueRepository.findByProjectId(projectId);
        for(ProjectImpactValue x : projectData){
            List<ProcessImpactValue> processData = processImpactValueRepository.findAllByProcessIdsAAndImpactMethodCategory(processIds,x.getImpactMethodCategory().getId());
            BigDecimal sum = BigDecimal.ZERO;
            for (ProcessImpactValue y : processData){
                sum = sum.add(y.getSystemLevel());
            }
            x.setValue(sum);
        }
        projectImpactValueRepository.saveAll(projectData);
    }
}
