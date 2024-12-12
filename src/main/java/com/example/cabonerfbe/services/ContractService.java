package com.example.cabonerfbe.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * The interface Contract service.
 *
 * @author SonPHH.
 */
public interface ContractService {
    /**
     * Download contract method.
     *
     * @param contractId the contract id
     * @return the response entity
     */
    ResponseEntity<Resource> downloadContract(UUID contractId);

    /**
     * Delete contract method.
     *
     * @param contractId the contract id
     */
    void deleteContract(UUID contractId);
}
