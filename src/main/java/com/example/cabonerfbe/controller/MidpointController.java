package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateFactorRequest;
import com.example.cabonerfbe.request.ExportFactorRequest;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.MidpointService;
import com.example.cabonerfbe.services.impl.ExcelServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.IMPACT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class MidpointController {
    @Autowired
    private MidpointService midpointService;
    @Autowired
    private ExcelServiceImpl excelService;

    @GetMapping(API_PARAMS.GET_ALL_MIDPOINT_FACTORS)
    public ResponseEntity<ResponseObject> getAllMidpointFactors() {
        log.info("Start getAllMidpointFactors");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_MIDPOINT_FACTORS_SUCCESS, midpointService.getAllMidpointFactors())
        );
    }

    @GetMapping(API_PARAMS.GET_MIDPOINT_FACTOR_BY_ID)
    public ResponseEntity<ResponseObject> getMidpointFactorById(@PathVariable UUID id) {
        log.info("Start getMidpointFactorById. id: {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_MIDPOINT_FACTOR_BY_ID_SUCCESS, midpointService.getMidpointFactorById(id))
        );
    }

    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.GET_ALL_MIDPOINT_FACTORS)
    public ResponseEntity<ResponseObject> getAllMidpointSubstanceFactorsAdmin(@Valid PaginationRequest request, @RequestParam(required = false) UUID compartmentId, @RequestParam(required = false) String keyword) {
        log.info("Start getAllMidpointSubstanceFactorsAdmin. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_MIDPOINT_SUBSTANCE_FACTORS_SUCCESS, midpointService.getAllMidpointFactorsAdmin(request, compartmentId, keyword))
        );
    }

    @PostMapping(API_PARAMS.ADMIN)
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody CreateFactorRequest request) {
        log.info("Start createFactor. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create factor success", midpointService.create(request))
        );
    }

    @DeleteMapping(API_PARAMS.ADMIN + API_PARAMS.DELETE_MIDPOINT_FACTOR_BY_ID)
    public ResponseEntity<ResponseObject> delete(@PathVariable UUID id) {
        log.info("Start deleteFactor. Id: {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Delete factor success", midpointService.delete(id))
        );
    }

    @PostMapping(API_PARAMS.ADMIN + API_PARAMS.IMPORT_MIDPOINT_FACTOR_BY_ID)
    public ResponseEntity<ResponseObject> importExcel(@RequestParam("file") MultipartFile file, @RequestParam String methodName) throws IOException {
        log.info("Start importExcel");
        return ResponseEntity.ok(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, "Import success", excelService.readExcel(file, methodName)
        ));
    }

    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.DOWNLOAD_ERROR_LOG_MIDPOINT_FACTOR_BY_ID)
    public ResponseEntity<Resource> downloadFileLog(@RequestParam String fileName) throws IOException {
        log.info("Start downloadFileLog . fileName: {}", fileName);
        return excelService.downloadErrorLog(fileName);
    }

    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.DOWNLOAD_TEMPLATE_MIDPOINT_FACTOR)
    public ResponseEntity<Resource> downloadFactorTemplate() throws IOException {
        log.info("Start downloadFactorTemplate");
        return excelService.downloadErrorLog("template/factor-template.xlsx");
    }

    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.EXPORT_MIDPOINT_FACTOR)
    public ResponseEntity<Resource> exportFactor(ExportFactorRequest request) {
        log.info("Start exportFactor");
        return excelService.exportFactor(request);
    }

}
