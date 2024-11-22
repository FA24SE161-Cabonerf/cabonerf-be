package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.converter.EmissionCompartmentConverter;
import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.models.EmissionCompartment;
import com.example.cabonerfbe.request.EmissionCompartmentRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.EmissionCompartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.EMISSIONS)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin("*")
@Slf4j
public class EmissionCompartmentController {
    @Autowired
    EmissionCompartmentService emissionCompartmentService;

    @GetMapping(API_PARAMS.GET_ALL_EMISSION_COMPARTMENTS)
    public ResponseEntity<ResponseObject> getAllEmissionCompartments() {
        log.info("Start getAllEmissionCompartments");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_EMISSION_COMPARTMENTS_SUCCESS, emissionCompartmentService.getAllEmissionCompartments())
        );
    }

    @PostMapping(API_PARAMS.GET_ALL_EMISSION_COMPARTMENTS)
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody EmissionCompartmentRequest request){
        log.info("Start createEmissionCompartment. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create emission compartment success", emissionCompartmentService.create(request))
        );
    }

    @PutMapping(API_PARAMS.GET_ALL_EMISSION_COMPARTMENTS + API_PARAMS.UPDATE_EMISSION_COMPARTMENT)
    public ResponseEntity<ResponseObject> update(@PathVariable UUID emissionCompartmentId, @Valid @RequestBody EmissionCompartmentRequest request){
        log.info("Start updateEmissionCompartment. id: {}, request: {}", emissionCompartmentId,request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Update emission compartment success", emissionCompartmentService.update(emissionCompartmentId,request))
        );
    }

    @DeleteMapping(API_PARAMS.GET_ALL_EMISSION_COMPARTMENTS + API_PARAMS.DELETE_EMISSION_COMPARTMENT)
    public ResponseEntity<ResponseObject> delete(@PathVariable UUID emissionCompartmentId){
        log.info("Start deleteEmissionCompartment. id : {}",emissionCompartmentId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Delete emission compartment success", emissionCompartmentService.delete(emissionCompartmentId))
        );
    }

}
