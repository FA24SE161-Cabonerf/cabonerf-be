package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.BaseImpactMethodRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ImpactMethodService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.IMPACT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ImpactMethodController {
    @Autowired
    private ImpactMethodService impactMethodService;

    @GetMapping(API_PARAMS.GET_ALL_IMPACT_METHODS)
    public ResponseEntity<ResponseObject> getAllImpactMethods() {
        log.info("Start getAllImpactMethods");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_IMPACT_METHODS_SUCCESS, impactMethodService.getAllImpactMethods())
        );
    }

    @GetMapping(API_PARAMS.GET_IMPACT_METHOD_BY_ID)
    public ResponseEntity<ResponseObject> getImpactMethodById(@PathVariable UUID methodId) {
        log.info("Start getImpactMethodById. id {}", methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_IMPACT_METHOD_BY_ID_SUCCESS, impactMethodService.getImpactMethodById(methodId))
        );
    }

    @GetMapping(API_PARAMS.GET_CATEGORY_BY_METHOD_ID)
    public ResponseEntity<ResponseObject> getCategoryByMethodId(@PathVariable UUID methodId) {
        log.info("Start getCategoryByMethodId. id {}", methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_CATEGORY_BY_METHOD_ID_SUCCESS, impactMethodService.getCategoryByMethodId(methodId))
        );
    }

    @PostMapping(API_PARAMS.CREATE_IMPACT_METHOD)
    public ResponseEntity<ResponseObject> createImpactMethod(@Valid @RequestBody BaseImpactMethodRequest request) {
        log.info("Start createImpactMethod. request {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_IMPACT_METHOD_SUCCESS, impactMethodService.createImpactMethod(request))
        );
    }

    @PutMapping(API_PARAMS.UPDATE_IMPACT_METHOD_BY_ID)
    public ResponseEntity<ResponseObject> updateImpactMethodById(@PathVariable UUID methodId, @Valid @RequestBody BaseImpactMethodRequest request) {
        log.info("Start updateImpactMethodById, methodId {}, request {}", methodId, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_IMPACT_METHOD_SUCCESS, impactMethodService.updateImpactMethodById(methodId, request))
        );
    }

    @DeleteMapping(API_PARAMS.DELETE_IMPACT_METHOD_BY_ID)
    public ResponseEntity<ResponseObject> deleteImpactMethodById(@PathVariable UUID methodId) {
        log.info("Start deleteImpactMethodById, methodId {}", methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.DELETE_IMPACT_METHOD_SUCCESS, impactMethodService.deleteImpactMethodById(methodId))
        );
    }

    @GetMapping(API_PARAMS.GET_ALL_METHOD_NAME)
    public ResponseEntity<ResponseObject> addImpactCategoryToImpactMethod() {
        log.info("Start getAllMethodName");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Get all method name success", impactMethodService.getNameAllMethod())
        );
    }

}
