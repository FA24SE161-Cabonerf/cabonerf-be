package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ImpactMethodService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResponseObject> getImpactMethodById(@PathVariable long id) {
        log.info("Start getImpactMethodById. id {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_IMPACT_METHOD_BY_ID_SUCCESS, impactMethodService.getImpactMethodById(id))
        );
    }

    @GetMapping(API_PARAMS.GET_CATEGORY_BY_METHOD_ID)
    public ResponseEntity<ResponseObject> getCategoryByMethodId(@PathVariable long id) {
        log.info("Start getCategoryByMethodId. id {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_CATEGORY_BY_METHOD_ID_SUCCESS, impactMethodService.getCategoryByMethodId(id))
        );
    }
}
