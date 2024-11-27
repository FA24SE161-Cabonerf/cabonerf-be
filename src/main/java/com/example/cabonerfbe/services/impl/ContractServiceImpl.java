package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ContractConverter;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Contract;
import com.example.cabonerfbe.repositories.ContractRepository;
import com.example.cabonerfbe.services.ContractService;
import com.example.cabonerfbe.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository repository;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private ContractConverter contractConverter;

    @Override
    public ResponseEntity<Resource> downloadContract(UUID contractId) {
        Contract c = repository.findById(contractId)
                .orElseThrow(() -> CustomExceptions.notFound("Contract not exist"));

        try {
            byte[] fileData = s3Service.downloadFile(c.getUrl());

            if (fileData != null && fileData.length > 0) {
                ByteArrayResource resource = new ByteArrayResource(fileData);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + c.getOrganization().getName() + "\"");


                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(fileData.length)
                        .body(resource);
            } else {
                throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "File not found in S3");
            }
        } catch (RuntimeException e) {
            throw e; // rethrow after logging

        }
    }

    @Override
    public void deleteContract(UUID contractId) {
        Contract c = repository.findById(contractId)
                .orElseThrow(() -> CustomExceptions.notFound("Contract not exist"));

        s3Service.downloadFile(c.getUrl());
        c.setStatus(false);
        repository.save(c);
    }
}
