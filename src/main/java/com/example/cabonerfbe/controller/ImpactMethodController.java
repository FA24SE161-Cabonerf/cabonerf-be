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

/**
 * The class Impact method controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.IMPACT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ImpactMethodController {
    @Autowired
    private ImpactMethodService impactMethodService;

    /**
     * Gets all impact methods.
     *
     * @return the all impact methods
     */
    @GetMapping(API_PARAMS.GET_ALL_IMPACT_METHODS)
    public ResponseEntity<ResponseObject> getAllImpactMethods() {
        log.info("Start getAllImpactMethods");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_IMPACT_METHODS_SUCCESS, impactMethodService.getAllImpactMethods())
        );
    }

    /**
     * Gets impact method by id.
     *
     * @param methodId the method id
     * @return the impact method by id
     */
    @GetMapping(API_PARAMS.GET_IMPACT_METHOD_BY_ID)
    public ResponseEntity<ResponseObject> getImpactMethodById(@PathVariable UUID methodId) {
        log.info("Start getImpactMethodById. id {}", methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_IMPACT_METHOD_BY_ID_SUCCESS, impactMethodService.getImpactMethodById(methodId))
        );
    }

    /**
     * Gets category by method id.
     *
     * @param methodId the method id
     * @return the category by method id
     */
    @GetMapping(API_PARAMS.GET_CATEGORY_BY_METHOD_ID)
    public ResponseEntity<ResponseObject> getCategoryByMethodId(@PathVariable UUID methodId) {
        log.info("Start getCategoryByMethodId. id {}", methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_CATEGORY_BY_METHOD_ID_SUCCESS, impactMethodService.getCategoryByMethodId(methodId))
        );
    }

    /**
     * Create impact method method.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping(API_PARAMS.CREATE_IMPACT_METHOD)
    public ResponseEntity<ResponseObject> createImpactMethod(@Valid @RequestBody BaseImpactMethodRequest request) {
        log.info("Start createImpactMethod. request {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_IMPACT_METHOD_SUCCESS, impactMethodService.createImpactMethod(request))
        );
    }

    /**
     * Update impact method by id method.
     *
     * @param methodId the method id
     * @param request  the request
     * @return the response entity
     */
    @PutMapping(API_PARAMS.UPDATE_IMPACT_METHOD_BY_ID)
    public ResponseEntity<ResponseObject> updateImpactMethodById(@PathVariable UUID methodId, @Valid @RequestBody BaseImpactMethodRequest request) {
        log.info("Start updateImpactMethodById, methodId {}, request {}", methodId, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_IMPACT_METHOD_SUCCESS, impactMethodService.updateImpactMethodById(methodId, request))
        );
    }

    /**
     * Delete impact method by id method.
     *
     * @param methodId the method id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.DELETE_IMPACT_METHOD_BY_ID)
    public ResponseEntity<ResponseObject> deleteImpactMethodById(@PathVariable UUID methodId) {
        log.info("Start deleteImpactMethodById, methodId {}", methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.DELETE_IMPACT_METHOD_SUCCESS, impactMethodService.deleteImpactMethodById(methodId))
        );
    }

    /**
     * Gets name all method.
     *
     * @return the name all method
     */
    @GetMapping(API_PARAMS.GET_ALL_METHOD_NAME)
    public ResponseEntity<ResponseObject> getNameAllMethod() {
        log.info("Start getAllMethodName");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get all method name success", impactMethodService.getNameAllMethod())
        );
    }

    /**
     * Add impact category to impact method method.
     *
     * @param methodId   the method id
     * @param categoryId the category id
     * @return the response entity
     */
    @PostMapping(API_PARAMS.ADD_IMPACT_CATEGORY_TO_IMPACT_METHOD)
    public ResponseEntity<ResponseObject> addImpactCategoryToImpactMethod(@PathVariable UUID methodId, @PathVariable UUID categoryId) {
        log.info("Start addImpactCategoryToImpactMethod");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Add impact category to impact method", impactMethodService.addImpactCategoryToImpactMethod(methodId, categoryId))
        );
    }

}
