package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.UnitGroupService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.UNIT_GROUP)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UnitGroupController {

    @Autowired
    private UnitGroupService unitGroupService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllUnitGroup() {
        log.info("Start getAllUnitGroup");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_UNIT_GROUP_SUCCESS, unitGroupService.getAllUnitGroup()
                ));
    }

    @GetMapping(API_PARAMS.UNIT_GROUP_BY_ID)
    public ResponseEntity<ResponseObject> getUnitGroupById(@PathVariable long id) {
        log.info("Start getUnitGroupById. id:{}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_UNIT_GROUP_BY_ID_SUCCESS, unitGroupService.getUnitGroupById(id)
                ));
    }

}
