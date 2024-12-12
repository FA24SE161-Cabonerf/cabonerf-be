package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.services.ContractService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The class Contract controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.CONTRACT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ContractController {
    @Autowired
    private ContractService contractService;

    /**
     * Download contract method.
     *
     * @param contractId the contract id
     * @return the response entity
     */
    @GetMapping(API_PARAMS.DOWNLOAD_CONTRACT)
    public ResponseEntity<Resource> downloadContract(@PathVariable("contractId") UUID contractId) {
        log.info("Start downloadContract. Id: {}", contractId);
        return contractService.downloadContract(contractId);
    }
}
