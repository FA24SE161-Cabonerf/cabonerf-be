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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

            double unitLevel = Constants.BASE_UNIT_LEVEL;

            for (Exchanges exchange : exchangeList) {
                UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
                double exchangeValue = exchange.getValue();

                Optional<MidpointImpactCharacterizationFactors> midpointFactorsOptional =
                        midpointFactorsRepository.findByMethodCategoryAndEmissionSubstance(methodCategoryId, emissionSubstanceId);

                // Calculate and add to unit level based on presence of midpoint factors
                unitLevel += midpointFactorsOptional
                        .map(midpointFactors -> exchangeValue * midpointFactors.getDecimalValue())
                        .orElse(Constants.BASE_UNIT_LEVEL);
            }

            processImpactValue.setUnitLevel(unitLevel);
            processImpactValueList.add(processImpactValue);
        }

        processImpactValueRepository.saveAll(processImpactValueList);
    }

    public void computeProcessImpactValueSingleExchange(Process process, Exchanges exchange, double initialValue) {
        UUID processId = process.getId();
        List<ProcessImpactValue> processImpactValueList = new ArrayList<>();

        UUID emissionSubstanceId = exchange.getEmissionSubstance().getId();
        Unit baseUnit = exchange.getEmissionSubstance().getUnit();

        List<MidpointImpactCharacterizationFactors> list = midpointFactorsRepository.findByEmissionSubstanceId(emissionSubstanceId);

        for (MidpointImpactCharacterizationFactors factors : list) {
            Optional<ProcessImpactValue> processImpactValue = processImpactValueRepository.findByProcessIdAndImpactMethodCategoryId(processId, factors.getImpactMethodCategory().getId());
            if (processImpactValue.isPresent()) {
                log.info("unit: {}, rate: {}", exchange.getUnit().getName(), exchange.getUnit().getConversionFactor());
                double unitLevel = processImpactValue.get().getUnitLevel();
                log.info("pre unitLevel: {} of factors impact category: {}", unitLevel, factors.getImpactMethodCategory().getImpactCategory().getName() );
                // converting to the base unit value
                double exchangeValue = unitService.convertValue(exchange.getUnit(), exchange.getValue() - initialValue, baseUnit);
                log.info("pre exchangeValue: {}, converted exchangeValue: {}, initValue (trước khi thay đổi unit/update): {}", exchange.getValue(), exchangeValue, initialValue );
                unitLevel += exchangeValue * factors.getDecimalValue();
                log.info("factor value: {}", factors.getDecimalValue());
                log.info("post unitLevel: {}", unitLevel );
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

    public void computeSystemLevelOfProject(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND)
        );

        // from projectId, gets all process within
        List<Process> processList = processRepository.findAll(projectId);
        if (processList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROCESS_IN_PROJECT);
        }
    }
}
