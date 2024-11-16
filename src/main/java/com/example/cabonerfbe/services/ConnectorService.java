package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.response.CreateConnectorResponse;
import com.example.cabonerfbe.response.DeleteConnectorResponse;

import java.util.UUID;

public interface ConnectorService {
    CreateConnectorResponse createConnector(CreateConnectorRequest request);

    DeleteConnectorResponse deleteConnector(UUID id);
}
