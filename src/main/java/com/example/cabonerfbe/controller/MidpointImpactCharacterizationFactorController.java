package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.impl.ExcelServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class MidpointImpactCharacterizationFactorController {

    @Autowired
    private ExcelServiceImpl excelService;

    @PostMapping(API_PARAMS.IMPORT_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR)
    public ResponseEntity<ResponseObject> importExcel(@RequestParam("file") MultipartFile file, @RequestParam String methodName) throws IOException {
        log.info("Start importExcel");
        excelService.readExcel(file,methodName);
        return ResponseEntity.ok(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,"Import success","[]"
        ));
    }
}
