package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ConnectorConverter;
import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.dto.ConnectorDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Connector;
import com.example.cabonerfbe.models.Exchanges;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.response.CreateConnectorResponse;
import com.example.cabonerfbe.services.ConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ConnectorServiceImpl implements ConnectorService {
    @Autowired
    private ConnectorRepository connectorRepository;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private ExchangesTypeRepository exchangesTypeRepository;
    @Autowired
    private ExchangesConverter exchangesConverter;
    @Autowired
    private ConnectorConverter connectorConverter;

    public static final long VALID_PROCESS_COUNT = 2;
    public static final String START_PROCESS = "startProcessId";
    public static final String END_PROCESS = "endProcessId";
    public static final String START_EXCHANGE = "startExchangeId";
    public static final String END_EXCHANGE = "endExchangeId";
    public static final boolean INPUT = true;
    public static final boolean OUTPUT = false;
    @Autowired
    private ProcessConverter processConverter;
    @Autowired
    private ProcessServiceImpl processServiceImpl;

    @Override
    public CreateConnectorResponse createConnector(CreateConnectorRequest request) {
        UUID startProcessId = request.getStartProcessId();
        UUID endProcessId = request.getEndProcessId();
        UUID startExchangeId = request.getStartExchangesId();
        UUID endExchangeId = request.getEndExchangesId();

        if (startProcessId.equals(endProcessId)) {
            throw CustomExceptions.badRequest(MessageConstants.START_END_PROCESS_SAME_ERROR);
        }

        Process startProcess = processRepository.findByProcessId(startProcessId).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND, Map.of(START_PROCESS, startProcessId))
        );

        Process endProcess = processRepository.findByProcessId(endProcessId).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND, Map.of(END_PROCESS, endProcessId))
        );

        // check if 2 processes belong to the same project
        if (!startProcess.getProject().getId().equals(endProcess.getProject().getId())) {
            throw CustomExceptions.badRequest(MessageConstants.PROCESS_IN_DIFFERENT_PROJECTS);
        }

        // check connector between start-end
        if (connectorRepository.existsByStartProcess_IdAndEndProcess_Id(startProcessId, endProcessId)) {
            throw CustomExceptions.badRequest(MessageConstants.CONNECTOR_ALREADY_EXIST);
        }

        boolean isEndProcessFlag = true;

        ConnectorDto connectorDto = new ConnectorDto();

        // start != null
        // 1. check exist
        // 2. check exchange có thuộc process ko
        // 3. check type product & start input = false? (gop chung voi 1)
        if (startExchangeId != null && endExchangeId != null) {
            // return exchange if found && type = product && status = true && input = false
            Exchanges startExchange = exchangesRepository.findByIdAndTypeAndInput(startExchangeId, Constants.PRODUCT_EXCHANGE, OUTPUT).orElseThrow(
                    () -> CustomExceptions.notFound(MessageConstants.INVALID_EXCHANGE, Map.of(START_EXCHANGE, startExchangeId))
            );

            // return exchange if found && type = product && status = true && input = true
            Exchanges endExchange = exchangesRepository.findByIdAndTypeAndInput(endExchangeId, Constants.PRODUCT_EXCHANGE, INPUT).orElseThrow(
                    () -> CustomExceptions.notFound(MessageConstants.INVALID_EXCHANGE, Map.of(END_EXCHANGE, endExchangeId))
            );

            if (!startExchange.getProcessId().equals(startProcessId) || !endExchange.getProcessId().equals(endProcessId)) {
                throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_AND_PROCESS_DIFFERENT);
            }


            String startExchangeUnitGroup = startExchange.getUnit().getUnitGroup().getName();
            String endExchangeUnitGroup = endExchange.getUnit().getUnitGroup().getName();
            String startExchangeName = startExchange.getName();
            String endExchangeName = endExchange.getName();

            if (!startExchangeUnitGroup.equals(endExchangeUnitGroup)) {
                throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_UNIT_GROUP_DIFFERENT);
            }

            if (!startExchangeName.equals(endExchangeName)) {
                throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_NAME_DIFFERENT);
            }

            connectorDto = convertConnector(startExchange, endExchange, startProcess, endProcess);
        } else if (startExchangeId != null) {
            // return exchange if found && type = product && status = true && input = false
            Exchanges startExchange = exchangesRepository.findByIdAndTypeAndInput(startExchangeId, Constants.PRODUCT_EXCHANGE, OUTPUT).orElseThrow(
                    () -> CustomExceptions.notFound(MessageConstants.INVALID_EXCHANGE, Map.of(START_EXCHANGE, startExchangeId))
            );

            if (!startExchange.getProcessId().equals(startProcessId)) {
                throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_AND_PROCESS_DIFFERENT);
            }

            // check if the end process already had exchange with the same name and unit group and input = true
            // yes: => add that one to connector
            // no: => create new

            Exchanges existEndExchange = exchangesRepository.findByProcess_IdAndNameAndUnit_UnitGroupAndInput(
                            endProcessId,
                            startExchange.getName(),
                            startExchange.getUnit().getUnitGroup(),
                            INPUT)
                    .orElseGet(() -> {
                                Exchanges newEndExchange = new Exchanges();
                                newEndExchange = exchangesConverter.fromExchangeToAnotherExchange(startExchange);
                                newEndExchange.setProcess(endProcess);
                                newEndExchange.setInput(INPUT);
                                return exchangesRepository.save(newEndExchange);
                            }
                    );

            connectorDto = convertConnector(startExchange, existEndExchange, startProcess, endProcess);
        } else if (endExchangeId != null) {
            // return exchange if found && type = product && status = true && input = true
            Exchanges endExchange = exchangesRepository.findByIdAndTypeAndInput(endExchangeId, Constants.PRODUCT_EXCHANGE, INPUT).orElseThrow(
                    () -> CustomExceptions.notFound(MessageConstants.INVALID_EXCHANGE, Map.of(END_EXCHANGE, endExchangeId))
            );

            if (!endExchange.getProcessId().equals(endProcessId)) {
                throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_AND_PROCESS_DIFFERENT);
            }

            // check if the start process already had exchange with the same name and unit group and input = false
            // yes: => add that one to connector
            // no: => create new

            Exchanges existStartExchange = exchangesRepository.findByProcess_IdAndNameAndUnit_UnitGroupAndInput(
                            endProcessId,
                            endExchange.getName(),
                            endExchange.getUnit().getUnitGroup(),
                            OUTPUT)
                    .orElseGet(() -> {
                                Exchanges newStartExchange = new Exchanges();
                                newStartExchange = exchangesConverter.fromExchangeToAnotherExchange(endExchange);
                                newStartExchange.setProcess(startProcess);
                                newStartExchange.setInput(OUTPUT);
                                return exchangesRepository.save(newStartExchange);
                            }
                    );
            connectorDto = convertConnector(existStartExchange, endExchange, startProcess, endProcess);
            isEndProcessFlag = false;
        } else {
            throw CustomExceptions.badRequest(MessageConstants.INVALID_EXCHANGE);
        }

        ProcessDto finalProcess = new ProcessDto();
        if (isEndProcessFlag) {
            finalProcess = processServiceImpl.getProcessById(endProcessId);
        } else {
            finalProcess = processServiceImpl.getProcessById(startProcessId);
        }

        CreateConnectorResponse response = new CreateConnectorResponse();
        response.setConnector(connectorDto);
        response.setProcess(finalProcess);

        return response;
    }

    private ConnectorDto convertConnector(Exchanges startExchange, Exchanges endExchange, Process startProcess, Process endProcess) {
        Connector connector = new Connector();
        connector.setStartProcess(startProcess);
        connector.setEndProcess(endProcess);
        connector.setStartExchanges(startExchange);
        connector.setEndExchanges(endExchange);

        return connectorConverter.fromConnectorToConnectorDto(connectorRepository.save(connector));
    }

}
