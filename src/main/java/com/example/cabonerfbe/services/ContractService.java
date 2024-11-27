package com.example.cabonerfbe.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ContractService {
    ResponseEntity<Resource> downloadContract(UUID contractId);

    void deleteContract(UUID contractId);
}
