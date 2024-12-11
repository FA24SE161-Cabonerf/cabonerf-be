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

import java.util.UUID;

/**
 * The class Unit controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UnitController {
    @Autowired
    private UnitService unitService;

    /**
     * Gets all units.
     *
     * @return the all units
     */
    @GetMapping(API_PARAMS.GET_ALL_UNITS)
    public ResponseEntity<ResponseObject> getAllUnits() {
        log.info("getAllUnits");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_UNITS_SUCCESS, unitService.getAllUnits())
        );
    }

    /**
     * Gets unit by id.
     *
     * @param unitId the unit id
     * @return the unit by id
     */
    @GetMapping(API_PARAMS.GET_UNIT_BY_ID)
    public ResponseEntity<ResponseObject> getUnitById(@PathVariable UUID unitId) {
        log.info("getUnitById: {}", unitId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_UNIT_BY_ID_SUCCESS, unitService.getUnitById(unitId))
        );
    }

    /**
     * Gets all units from group id.
     *
     * @param groupId the group id
     * @return the all units from group id
     */
    @GetMapping(API_PARAMS.GET_ALL_UNITS_FROM_UNIT_GROUP_ID)
    public ResponseEntity<ResponseObject> getAllUnitsFromGroupId(@PathVariable UUID groupId) {
        log.info("getAllUnitsFromGroupId: {}", groupId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_UNITS_SUCCESS, unitService.getAllUnitsFromGroupId(groupId))
        );
    }

    /**
     * Create unit in unit group method.
     *
     * @param groupId the group id
     * @param request the request
     * @return the response entity
     */
    @PostMapping(API_PARAMS.ADD_UNIT_TO_UNIT_GROUP)
    public ResponseEntity<ResponseObject> createUnitInUnitGroup(@PathVariable UUID groupId, @Valid @RequestBody CreateUnitRequest request) {
        log.info("addUnitToUnitGroup: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.ADD_UNIT_TO_UNIT_GROUP_SUCCESS, unitService.createUnitInUnitGroup(groupId, request))
        );
    }

    /**
     * Update unit by id method.
     *
     * @param unitId  the unit id
     * @param request the request
     * @return the response entity
     */
    @PutMapping(API_PARAMS.UPDATE_UNIT_BY_ID)
    public ResponseEntity<ResponseObject> updateUnitById(@PathVariable UUID unitId, @Valid @RequestBody UpdateUnitRequest request) {
        log.info("updateUnitById: {}", unitId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_UNIT_BY_ID_SUCCESS, unitService.updateUnitById(unitId, request))
        );
    }

    /**
     * Delete unit by id method.
     *
     * @param unitId the unit id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.DELETE_UNIT_BY_ID)
    public ResponseEntity<ResponseObject> deleteUnitById(@PathVariable UUID unitId) {
        log.info("deleteUnitById: {}", unitId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.DELETE_UNIT_BY_ID_SUCCESS, unitService.deleteUnitById(unitId))
        );
    }

}
