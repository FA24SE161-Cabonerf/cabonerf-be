package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.MidpointImpactCategoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.IMPACT)
@RestController
@AllArgsConstructor
@NoArgsConstructor
@CrossOrigin("*")
@Slf4j
public class MidpointImpactCategoryController {
    @Autowired
    MidpointImpactCategoryService midpointImpactCategoryService;

    @GetMapping(API_PARAMS.GET_ALL_MIDPOINT_IMPACT_CATEGORY)
    public ResponseEntity<ResponseObject> getAllMidpointImpactCategories() {
        log.info("Start getAllMidpointImpactCategories");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_MIDPOINT_IMPACT_CATEGORY_SUCCESS, midpointImpactCategoryService.getAllMidpointImpactCategories())
        );
    }
}