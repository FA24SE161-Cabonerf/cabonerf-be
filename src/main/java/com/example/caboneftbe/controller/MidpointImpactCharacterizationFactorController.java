package com.example.caboneftbe.controller;

import com.example.caboneftbe.converter.MidpointImpactCharacterizationFactorConverterImpl;
import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.request.ImportEmissionSubstanceRequest;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.impl.ExcelServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class MidpointImpactCharacterizationFactorController {

    @Autowired
    private ExcelServiceImpl service;

    @PostMapping(API_PARAMS.IMPORT_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR)
    public ResponseEntity<ResponseObject> importExcel(@RequestParam("file") MultipartFile file, @RequestParam String request){
        return ResponseEntity.ok(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,"Import success",service.readExcel(file,request)
        ));
    }
}
