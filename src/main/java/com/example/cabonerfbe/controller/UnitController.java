package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateUnitRequest;
import com.example.cabonerfbe.request.UpdateUnitRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.UnitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UnitController {
    @Autowired
    private UnitService unitService;

    @GetMapping(API_PARAMS.GET_ALL_UNITS)
    public ResponseEntity<ResponseObject> getAllUnits() {
        log.info("getAllUnits");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_UNITS_SUCCESS, unitService.getAllUnits())
        );
    }

    @GetMapping(API_PARAMS.GET_UNIT_BY_ID)
    public ResponseEntity<ResponseObject> getUnitById(@PathVariable Long unitId) {
        log.info("getUnitById: {}", unitId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_UNIT_BY_ID_SUCCESS, unitService.getUnitById(unitId))
        );
    }

    @GetMapping(API_PARAMS.GET_ALL_UNITS_FROM_UNIT_GROUP_ID)
    public ResponseEntity<ResponseObject> getAllUnitsFromGroupId(@PathVariable long groupId) {
        log.info("getAllUnitsFromGroupId: {}", groupId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_UNITS_SUCCESS, unitService.getAllUnitsFromGroupId(groupId))
        );
    }

    // TODO sửa lại path + validate các fields
    @PostMapping(API_PARAMS.ADD_UNIT_TO_UNIT_GROUP)
    public ResponseEntity<ResponseObject> createUnitInUnitGroup(@PathVariable Long groupId, @Valid @RequestBody CreateUnitRequest request) {
        log.info("addUnitToUnitGroup: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.ADD_UNIT_TO_UNIT_GROUP_SUCCESS, unitService.createUnitInUnitGroup(groupId ,request))
        );
    }

    @PutMapping(API_PARAMS.UPDATE_UNIT_BY_ID)
    public ResponseEntity<ResponseObject> updateUnitById(@PathVariable Long unitId, @Valid @RequestBody UpdateUnitRequest request) {
        log.info("updateUnitById: {}", unitId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_UNIT_BY_ID_SUCCESS, unitService.updateUnitById(unitId, request))
        );
    }

    @DeleteMapping(API_PARAMS.DELETE_UNIT_BY_ID)
    public ResponseEntity<ResponseObject> deleteUnitById(@PathVariable Long unitId) {
        log.info("deleteUnitById: {}", unitId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.DELETE_UNIT_BY_ID_SUCCESS, unitService.deleteUnitById(unitId))
        );
    }

}
