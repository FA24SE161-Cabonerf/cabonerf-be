package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.dto.ConnectorDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Connector;
import com.example.cabonerfbe.models.Exchanges;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.Unit;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.response.CreateConnectorResponse;
import com.example.cabonerfbe.services.ConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return null;
    }

}
