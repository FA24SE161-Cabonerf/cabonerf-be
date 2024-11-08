package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ExchangesService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.EMISSION_SUBSTANCE)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin("*")
@Slf4j
public class SubstanceController {
    @Autowired
    private ExchangesService service;

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllEmissionSubstances(
            @RequestParam(defaultValue = "1") int pageCurrent,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = true) UUID methodId,
            @RequestParam(required = true) boolean input,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID emissionCompartmentId,
            @RequestParam(required = false) UUID impactCategoryId
            ) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Get substance success",service.search(pageCurrent, pageSize, keyword, methodId, emissionCompartmentId, impactCategoryId,input))
        );
    }

    @GetMapping(API_PARAMS.ADMIN)
    public ResponseEntity<ResponseObject> getAllEmissionSubstancesInAdmin( @RequestParam(required = false) String keyword){
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Get substance success", service.getAllAdmin(keyword))
        );
    }

}
