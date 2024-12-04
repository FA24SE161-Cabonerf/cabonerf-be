package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.request.IndustryCodeRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.IndustryCodeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.INDUSTRY_CODE)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class IndustryCodeController {
    @Autowired
    private IndustryCodeService service;

    @GetMapping(API_PARAMS.MANAGER)
    public ResponseEntity<ResponseObject> get(@RequestParam(defaultValue = "1") int pageCurrent,
                                               @RequestParam(defaultValue = "5") int pageSize,
                                               @RequestParam(required = false) String keyword){
        log.info("Start getAllIndustryCode");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get all industry code success",service.getAll(pageCurrent, pageSize, keyword))
        );
    }

    @PostMapping(API_PARAMS.MANAGER)
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody IndustryCodeRequest request){
        log.info("Start createIndustryCode. request: {}",request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Create industry code success", service.create(request))
        );
    }

    @PutMapping(API_PARAMS.MANAGER + API_PARAMS.UPDATE_INDUSTRY_CODE)
    public ResponseEntity<ResponseObject> update(@PathVariable UUID industryCodeId, @Valid @RequestBody IndustryCodeRequest request){
        log.info("Start updateIndustryCode. id: {}, request: {}", industryCodeId, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Update industry code success",service.update(industryCodeId,request))
        );
    }

    @DeleteMapping(API_PARAMS.MANAGER + API_PARAMS.DELETE_INDUSTRY_CODE)
    public ResponseEntity<ResponseObject> delete(@PathVariable UUID industryCodeId){
        log.info("Start deleteIndustryCode. id: {}", industryCodeId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Delete industry code success",service.delete(industryCodeId))
        );
    }

    @GetMapping(API_PARAMS.GET_INDUSTRY_CODE_BY_ORGANIZATION)
    public ResponseEntity<ResponseObject> getByOrganization(@PathVariable UUID organizationId){
        log.info("Start getIndustryCodeByOrganization. organizationId: {}",organizationId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get industry code by organization success", service.getInOrganization(organizationId))
        );
    }
}
