package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.UnitService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.UNIT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class UnitController {
    @Autowired
    private UnitService service;

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllProcess(
                @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "0") long unitGroupId) {
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Get all unit success", service.getAllUnit(currentPage, pageSize, unitGroupId)
                )
        );
    }

    @GetMapping(API_PARAMS.UNIT_BY_ID)
    public ResponseEntity<ResponseObject> getProcessById(@PathVariable long id) {
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Get unit by id success", service.getById(id)
                )
        );
    }
}
