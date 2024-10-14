package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.enums.MessageConstants;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.MidpointService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.IMPACT)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class MidpointController {
    @Autowired
    private MidpointService midpointService;

    @GetMapping(API_PARAMS.GET_ALL_MIDPOINT_FACTORS)
    public ResponseEntity<ResponseObject> getAllMidpointFactors() {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_MIDPOINT_FACTORS_SUCCESS, midpointService.getAllImpactCharacterizationFactors())
        );
    }

    @GetMapping(API_PARAMS.GET_MIDPOINT_FACTOR_BY_ID)
    public ResponseEntity<ResponseObject> getMidpointFactorById(@PathVariable long id) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_MIDPOINT_FACTOR_BY_ID_SUCCESS, midpointService.getImpactCharacterizationFactorById(id))
        );
    }
}
