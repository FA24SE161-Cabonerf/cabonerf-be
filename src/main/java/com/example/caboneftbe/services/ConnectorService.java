package com.example.caboneftbe.services;

import com.example.caboneftbe.request.CreateConnectorRequest;
import com.example.caboneftbe.response.CreateConnectorResponse;

public interface ConnectorService {
    CreateConnectorResponse createConnector(CreateConnectorRequest request);
}
