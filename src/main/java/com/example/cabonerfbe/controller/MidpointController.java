package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.MidpointService;
import jakarta.validation.Valid;
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
public class MidpointController {
    @Autowired
    private MidpointService midpointService;

    @GetMapping(API_PARAMS.GET_ALL_MIDPOINT_FACTORS)
    public ResponseEntity<ResponseObject> getAllMidpointFactors() {
        log.info("Start getAllMidpointFactors");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_MIDPOINT_FACTORS_SUCCESS, midpointService.getAllMidpointFactors())
        );
    }

    @GetMapping(API_PARAMS.GET_MIDPOINT_FACTOR_BY_ID)
    public ResponseEntity<ResponseObject> getMidpointFactorById(@PathVariable long id) {
        log.info("Start getMidpointFactorById. id: {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_MIDPOINT_FACTOR_BY_ID_SUCCESS, midpointService.getMidpointFactorById(id))
        );
    }

    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.GET_ALL_MIDPOINT_FACTORS)
    public ResponseEntity<ResponseObject> getAllMidpointSubstanceFactorsAdmin(@Valid PaginationRequest request) {
        log.info("Start getAllMidpointSubstanceFactorsAdmin. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_MIDPOINT_SUBSTANCE_FACTORS_SUCCESS, midpointService.getAllMidpointFactorsAdmin(request))
        );
    }

}
