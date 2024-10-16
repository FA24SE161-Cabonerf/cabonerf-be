package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.response.CreateConnectorResponse;

public interface ConnectorService {
    CreateConnectorResponse createConnector(CreateConnectorRequest request);
}
