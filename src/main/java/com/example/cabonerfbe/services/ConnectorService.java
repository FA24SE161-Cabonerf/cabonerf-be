package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.response.CreateConnectorResponse;
import com.example.cabonerfbe.response.DeleteConnectorResponse;

import java.util.UUID;

/**
 * The interface Connector service.
 *
 * @author SonPHH.
 */
public interface ConnectorService {
    /**
     * Create connector method.
     *
     * @param request the request
     * @return the create connector response
     */
    CreateConnectorResponse createConnector(CreateConnectorRequest request);

    /**
     * Delete connector method.
     *
     * @param id the id
     * @return the delete connector response
     */
    DeleteConnectorResponse deleteConnector(UUID id);
}
