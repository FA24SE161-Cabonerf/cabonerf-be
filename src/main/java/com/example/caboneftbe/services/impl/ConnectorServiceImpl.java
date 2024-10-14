package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.ConnectorConverter;
import com.example.caboneftbe.dto.ConnectorDto;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.Connector;
import com.example.caboneftbe.models.Exchanges;
import com.example.caboneftbe.models.Process;
import com.example.caboneftbe.models.Unit;
import com.example.caboneftbe.repositories.*;
import com.example.caboneftbe.request.CreateConnectorRequest;
import com.example.caboneftbe.request.CreateProductRequest;
import com.example.caboneftbe.response.CreateConnectorResponse;
import com.example.caboneftbe.services.ConnectorService;
import com.example.caboneftbe.services.ExchangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public CreateConnectorResponse createConnector(CreateConnectorRequest request) {

        Optional<Process> startProcess = processRepository.findById(request.getStartProcessId());
        Optional<Process> endProcess = processRepository.findById(request.getEndProcessId());

        Optional<Exchanges> startExchange = exchangesRepository.findById(request.getStartExchangesId());
        Optional<Exchanges> endExchange = Optional.of(new Exchanges());

        if (startProcess.isEmpty()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Start process not exist");
        }
        if (endProcess.isEmpty()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "End process not exist");
        }
//        if(!Objects.equals(startProcess.get().getProject().getId(), endProcess.get().getProject().getId())){
//            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Process not in the same project");
//        }
        if (startExchange.isEmpty()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Start exchange not exist");
        }
        if (startExchange.get().getExchangesType().getId() != 1) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Start exchange is elementary can not create connector");
        }
        if (startExchange.get().isInput()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Start exchange cannot input");
        }
        if (request.getEndExchangesId() != null) {
            endExchange = exchangesRepository.findById(request.getEndExchangesId());
            if (endExchange.isEmpty()) {
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "End exchange not exist");
            }
            if (endExchange.get().getExchangesType().getId() != 1) {
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "End exchange is elementary can not create connector");
            }
            if (startExchange.get().getProcess().getId() == endExchange.get().getProcess().getId()) {
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Cannot connect exchanges in 1 process");
            }
            if (!endExchange.get().isInput()) {
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "End exchange cannot out");
            }
            if (!Objects.equals(startExchange.get().getName(), endExchange.get().getName())) {
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Two exchange not equal");
            }
            if (checkExistConnect(endExchange.get().getId())) {
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "End exchange already connect");
            }
            Connector connector = new Connector(startProcess.get(), endProcess.get(), startExchange.get(), endExchange.get());
            connector = connectorRepository.save(connector);
            ConnectorDto connectorDto = new ConnectorDto(connector.getId(), connector.getStartProcess().getId(), connector.getEndProcess().getId(), connector.getStartExchanges().getId(), connector.getEndExchanges().getId());
            return CreateConnectorResponse.builder()
                    .connector(connectorDto)
                    .build();
        }
        endExchange = Optional.of(createEndExchange(endProcess.get().getId(), startExchange.get().getName(), startExchange.get().getUnit()));
        Connector connector = new Connector(startProcess.get(), endProcess.get(), startExchange.get(), endExchange.get());
        connector = connectorRepository.save(connector);
        ConnectorDto connectorDto = new ConnectorDto(connector.getId(), connector.getStartProcess().getId(), connector.getEndProcess().getId(), connector.getStartExchanges().getId(), connector.getEndExchanges().getId());
        return CreateConnectorResponse.builder()
                .connector(connectorDto)
                .build();
    }

    private Exchanges createEndExchange(long processId, String name, Unit unit) {
        Optional<Process> process = processRepository.findById(processId);
        if (process.isEmpty()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Process not exist");
        }

        Exchanges exchanges = new Exchanges();
        exchanges.setExchangesType(exchangesTypeRepository.findById(1L).get());
        exchanges.setName(name);
        exchanges.setProcess(process.get());
        exchanges.setValue(0);
        exchanges.setUnit(unit);
        exchanges.setInput(true);

        exchanges = exchangesRepository.save(exchanges);

        return exchanges;
    }

    private boolean checkExistConnect(long endExchangeId) {
        Connector connector = connectorRepository.checkExist(endExchangeId);
        return connector != null;
    }
}
