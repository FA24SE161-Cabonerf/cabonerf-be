package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ConnectorConverter;
import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.dto.ConnectorDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Connector;
import com.example.cabonerfbe.models.Exchanges;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.ConnectorRepository;
import com.example.cabonerfbe.repositories.ExchangesRepository;
import com.example.cabonerfbe.repositories.ProcessRepository;
import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.response.CreateConnectorResponse;
import com.example.cabonerfbe.services.ConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ConnectorServiceImpl implements ConnectorService {

    private static final long VALID_PROCESS_COUNT = 2;
    private static final String START_PROCESS = "startProcessId";
    private static final String END_PROCESS = "endProcessId";
    private static final String START_EXCHANGE = "startExchangeId";
    private static final String END_EXCHANGE = "endExchangeId";
    private static final boolean INPUT = true;
    private static final boolean OUTPUT = false;

    @Autowired
    private ConnectorRepository connectorRepository;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private ExchangesConverter exchangesConverter;
    @Autowired
    private ConnectorConverter connectorConverter;
    @Autowired
    private ProcessServiceImpl processServiceImpl;

    @Override
    public CreateConnectorResponse createConnector(CreateConnectorRequest request) {
        Process startProcess = validateAndFetchProcess(request.getStartProcessId(), START_PROCESS);
        Process endProcess = validateAndFetchProcess(request.getEndProcessId(), END_PROCESS);

        if (startProcess.equals(endProcess)) {
            throw CustomExceptions.badRequest(MessageConstants.START_END_PROCESS_SAME_ERROR);
        }

        validateProcessesBelongToSameProject(startProcess, endProcess);
        validateNoExistingConnector(startProcess.getId(), endProcess.getId());

        ConnectorDto connectorDto;
        boolean isEndProcessFlag = true;

        if (request.getStartExchangesId() != null && request.getEndExchangesId() != null) {
            connectorDto = createConnectorWithBothExchanges(request, startProcess, endProcess);
        } else if (request.getStartExchangesId() != null) {
            connectorDto = createConnectorWithStartExchangeOnly(request, startProcess, endProcess);
        } else if (request.getEndExchangesId() != null) {
            connectorDto = createConnectorWithEndExchangeOnly(request, startProcess, endProcess);
            isEndProcessFlag = false;
        } else {
            throw CustomExceptions.badRequest(MessageConstants.INVALID_EXCHANGE);
        }

        ProcessDto finalProcess = isEndProcessFlag
                ? processServiceImpl.getProcessById(request.getEndProcessId())
                : processServiceImpl.getProcessById(request.getStartProcessId());

        return new CreateConnectorResponse(connectorDto, finalProcess);
    }

    private Process validateAndFetchProcess(UUID processId, String processType) {
        return processRepository.findByProcessId(processId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND, Map.of(processType, processId)));
    }

    private void validateProcessesBelongToSameProject(Process startProcess, Process endProcess) {
        if (!startProcess.getProject().getId().equals(endProcess.getProject().getId())) {
            throw CustomExceptions.badRequest(MessageConstants.PROCESS_IN_DIFFERENT_PROJECTS);
        }
    }

    private void validateNoExistingConnector(UUID startProcessId, UUID endProcessId) {
        if (connectorRepository.existsByStartProcess_IdAndEndProcess_Id(startProcessId, endProcessId)) {
            throw CustomExceptions.badRequest(MessageConstants.CONNECTOR_ALREADY_EXIST);
        }
    }

    private ConnectorDto createConnectorWithBothExchanges(CreateConnectorRequest request, Process startProcess, Process endProcess) {
        Exchanges startExchange = validateAndFetchExchange(request.getStartExchangesId(), OUTPUT, START_EXCHANGE);
        Exchanges endExchange = validateAndFetchExchange(request.getEndExchangesId(), INPUT, END_EXCHANGE);

        validateExchangeBelongsToProcess(startExchange, startProcess, START_EXCHANGE);
        validateExchangeBelongsToProcess(endExchange, endProcess, END_EXCHANGE);
        validateExchangeCompatibility(startExchange, endExchange);

        return convertAndSaveConnector(startExchange, endExchange, startProcess, endProcess);
    }

    private ConnectorDto createConnectorWithStartExchangeOnly(CreateConnectorRequest request, Process startProcess, Process endProcess) {
        Exchanges startExchange = validateAndFetchExchange(request.getStartExchangesId(), OUTPUT, START_EXCHANGE);
        validateExchangeBelongsToProcess(startExchange, startProcess, START_EXCHANGE);

        Exchanges endExchange = exchangesRepository.findByProcess_IdAndNameAndUnit_UnitGroupAndInput(
                        endProcess.getId(), startExchange.getName(), startExchange.getUnit().getUnitGroup(), INPUT)
                .orElseGet(() -> createNewExchange(startExchange, endProcess, INPUT));

        return convertAndSaveConnector(startExchange, endExchange, startProcess, endProcess);
    }

    private ConnectorDto createConnectorWithEndExchangeOnly(CreateConnectorRequest request, Process startProcess, Process endProcess) {
        Exchanges endExchange = validateAndFetchExchange(request.getEndExchangesId(), INPUT, END_EXCHANGE);
        validateExchangeBelongsToProcess(endExchange, endProcess, END_EXCHANGE);

        Exchanges startExchange = exchangesRepository.findByProcess_IdAndNameAndUnit_UnitGroupAndInput(
                        startProcess.getId(), endExchange.getName(), endExchange.getUnit().getUnitGroup(), OUTPUT)
                .orElseGet(() -> createNewExchange(endExchange, startProcess, OUTPUT));

        return convertAndSaveConnector(startExchange, endExchange, startProcess, endProcess);
    }

    private Exchanges validateAndFetchExchange(UUID exchangeId, boolean inputStatus, String exchangeType) {
        return exchangesRepository.findByIdAndTypeAndInput(exchangeId, Constants.PRODUCT_EXCHANGE, inputStatus)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.INVALID_EXCHANGE, Map.of(exchangeType, exchangeId)));
    }

    private void validateExchangeBelongsToProcess(Exchanges exchange, Process process, String exchangeType) {
        if (!exchange.getProcessId().equals(process.getId())) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_AND_PROCESS_DIFFERENT);
        }
    }

    private void validateExchangeCompatibility(Exchanges startExchange, Exchanges endExchange) {
        if (!startExchange.getUnit().getUnitGroup().equals(endExchange.getUnit().getUnitGroup())) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_UNIT_GROUP_DIFFERENT);
        }
        if (!startExchange.getName().equals(endExchange.getName())) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_NAME_DIFFERENT);
        }
    }

    private Exchanges createNewExchange(Exchanges originalExchange, Process process, boolean inputStatus) {
        Exchanges newExchange = exchangesConverter.fromExchangeToAnotherExchange(originalExchange);
        newExchange.setProcess(process);
        newExchange.setInput(inputStatus);
        return exchangesRepository.save(newExchange);
    }

    private ConnectorDto convertAndSaveConnector(Exchanges startExchange, Exchanges endExchange, Process startProcess, Process endProcess) {
        Connector connector = new Connector();
        connector.setStartProcess(startProcess);
        connector.setEndProcess(endProcess);
        connector.setStartExchanges(startExchange);
        connector.setEndExchanges(endExchange);

        return connectorConverter.fromConnectorToConnectorDto(connectorRepository.save(connector));
    }
}
