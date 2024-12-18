package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.converter.ConnectorConverter;
import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.dto.ConnectorDto;
import com.example.cabonerfbe.dto.ConnectorUpdatedProcessDto;
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
import com.example.cabonerfbe.response.DeleteConnectorResponse;
import com.example.cabonerfbe.services.ConnectorService;
import com.example.cabonerfbe.services.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The class Connector service.
 *
 * @author SonPHH.
 */
@Service
@Slf4j
public class ConnectorServiceImpl implements ConnectorService {

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
    private MessagePublisher messagePublisher;

    @Override
    public CreateConnectorResponse createConnector(CreateConnectorRequest request) {
        Process startProcess = validateAndFetchProcess(request.getStartProcessId(), START_PROCESS);
        Process endProcess = validateAndFetchProcess(request.getEndProcessId(), END_PROCESS);

        if (startProcess.equals(endProcess)) {
            throw CustomExceptions.badRequest(MessageConstants.START_END_PROCESS_SAME_ERROR);
        }

        validateProcessesBelongToSameProject(startProcess, endProcess);
        validateNoExistingConnector(startProcess.getId(), endProcess.getId());

        CreateConnectorResponse response;

        // Case 1: Both exchange
        // Case 2: Start exchange only
        // Case 3: End exchange only
        if (request.getStartExchangesId() != null && request.getEndExchangesId() != null) {
            response = createConnectorWithBothExchanges(request, startProcess, endProcess);
        } else if (request.getStartExchangesId() != null) {
            response = createConnectorWithStartExchangeOnly(request, startProcess, endProcess);
        } else if (request.getEndExchangesId() != null) {
            response = createConnectorWithEndExchangeOnly(request, startProcess, endProcess);
        } else {
            throw CustomExceptions.badRequest(MessageConstants.INVALID_EXCHANGE);
        }
        return response;
    }

    @Override
    public DeleteConnectorResponse deleteConnector(UUID id) {
        Connector connector = connectorRepository.findByIdAndStatus(id, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.CONNECTOR_NOT_FOUND)
        );
        connector.setStatus(Constants.STATUS_FALSE);
        connectorRepository.save(connector);

        return new DeleteConnectorResponse(id);
    }

    private Process validateAndFetchProcess(UUID processId, String processType) {
        Process process = processRepository.findByProcessId(processId)
                .orElseThrow(() -> {
                    return CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND, Map.of(processType, processId));
                });
        if (process.isLibrary() && END_PROCESS.equals(processType)) {
            throw CustomExceptions.badRequest(MessageConstants.CANNOT_CREATE_CONNECTOR_TO_OBJECT_LIBRARY_PROCESS);
        }
        return process;
    }

    /**
     * Delete associated connectors method.
     *
     * @param id         the id
     * @param actionType the action type
     */
    public void deleteAssociatedConnectors(UUID id, String actionType) {
        List<UUID> idList = new ArrayList<>();
        if (Constants.DELETE_CONNECTOR_TYPE_EXCHANGE.equals(actionType)) {
            List<Connector> associatedConnectorList = connectorRepository.findConnectorToExchange(id).stream().peek(
                    connector -> {
                        connector.setStatus(Constants.STATUS_FALSE);
                        idList.add(connector.getId());
                    }
            ).toList();
            connectorRepository.saveAll(associatedConnectorList);
        }
        if (Constants.DELETE_CONNECTOR_TYPE_PROCESS.equals(actionType)) {
            List<Connector> associatedConnectorList = connectorRepository.findConnectorToProcess(id).stream().peek(
                    connector -> {
                        connector.setStatus(Constants.STATUS_FALSE);
                        idList.add(connector.getId());
                    }
            ).toList();
            connectorRepository.saveAll(associatedConnectorList);
        }

        // publish message to rabbit to find and delete connectors on node based server
        messagePublisher.publishConnectorMessage(RabbitMQConfig.CONNECTOR_EXCHANGE, RabbitMQConfig.CONNECTOR_ROUTING_KEY, idList);
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

    private CreateConnectorResponse createConnectorWithBothExchanges(CreateConnectorRequest request, Process startProcess, Process endProcess) {
        Exchanges startExchange = validateAndFetchExchange(request.getStartExchangesId(), OUTPUT, START_EXCHANGE);
        Exchanges endExchange = validateAndFetchExchange(request.getEndExchangesId(), INPUT, END_EXCHANGE);
        validateExchangeBelongsToProcess(startExchange, startProcess, START_EXCHANGE);
        validateExchangeBelongsToProcess(endExchange, endProcess, END_EXCHANGE);
        validateExchangeCompatibility(startExchange, endExchange);
        validateEndExchangeAlreadyHadConnection(endExchange);
        return CreateConnectorResponse.builder()
                .connector(convertAndSaveConnector(startExchange, endExchange, startProcess, endProcess))
                .updatedProcess(null)
                .build();
    }


    private CreateConnectorResponse createConnectorWithStartExchangeOnly(CreateConnectorRequest request, Process startProcess, Process endProcess) {
        Exchanges startExchange = validateAndFetchExchange(request.getStartExchangesId(), OUTPUT, START_EXCHANGE);
        validateExchangeBelongsToProcess(startExchange, startProcess, START_EXCHANGE);

        // if the end exchange is not specified -> create new end
        Exchanges endExchange = createNewExchange(startExchange, endProcess, INPUT);

        return CreateConnectorResponse.builder()
                .connector(convertAndSaveConnector(startExchange, endExchange, startProcess, endProcess))
                .updatedProcess(new ConnectorUpdatedProcessDto(endProcess.getId(), exchangesConverter.fromExchangesToExchangesDto(endExchange)))
                .build();
    }

    private CreateConnectorResponse createConnectorWithEndExchangeOnly(CreateConnectorRequest request, Process startProcess, Process endProcess) {
        Exchanges endExchange = validateAndFetchExchange(request.getEndExchangesId(), INPUT, END_EXCHANGE);
        validateExchangeBelongsToProcess(endExchange, endProcess, END_EXCHANGE);
        validateEndExchangeAlreadyHadConnection(endExchange);
        Exchanges startExchange = exchangesRepository.findByProcess_IdAndNameAndUnit_UnitGroupAndInput(
                        startProcess.getId(), endExchange.getName(), endExchange.getUnit().getUnitGroup(), OUTPUT)
                .orElseGet(() -> createNewExchange(endExchange, startProcess, OUTPUT));
        return CreateConnectorResponse.builder()
                .connector(convertAndSaveConnector(startExchange, endExchange, startProcess, endProcess))
                .updatedProcess(new ConnectorUpdatedProcessDto(startProcess.getId(), exchangesConverter.fromExchangesToExchangesDto(startExchange)))
                .build();
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
        if (!startExchange.getUnit().getUnitGroup().getId().equals(endExchange.getUnit().getUnitGroup().getId())) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_UNIT_GROUP_DIFFERENT);
        }
        if (!startExchange.getName().equals(endExchange.getName())) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_NAME_DIFFERENT);
        }
    }

    private void validateEndExchangeAlreadyHadConnection(Exchanges endExchange) {
        if (connectorRepository.existsByEndExchanges_Id(endExchange.getId())) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_ALREADY_HAD_CONNECTION);
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
