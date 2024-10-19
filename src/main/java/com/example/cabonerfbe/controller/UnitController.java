package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.UnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.UNIT)
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UnitController {
    @Autowired
    private UnitService unitService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllUnitByUnitGroupId(
                @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "0") long unitGroupId) {
        log.info("Get getAllUnitByUnitGroupId. CurrentPage: {}, PageSize: {}, UnitGroupId: {}",currentPage, pageSize, unitGroupId);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Get all unit success", unitService.getAllUnit(currentPage, pageSize, unitGroupId)
                )
        );
    }

    @GetMapping(API_PARAMS.UNIT_BY_ID)
    public ResponseEntity<ResponseObject> getUnitById(@PathVariable long id) {
        log.info("Get getUnitById. Id: {}", id);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Get unit by id success", unitService.getById(id)
                )
        );
    }
}
