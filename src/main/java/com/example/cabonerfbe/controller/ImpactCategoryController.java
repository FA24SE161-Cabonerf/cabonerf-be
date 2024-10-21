package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ImpactCategoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.IMPACT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class ImpactCategoryController {
    @Autowired
    private ImpactCategoryService impactCategoryService;

    @GetMapping(API_PARAMS.GET_ALL_IMPACT_CATEGORIES)
    public ResponseEntity<ResponseObject> getImpactCategoryList() {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_IMPACT_CATEGORIES_SUCCESS, impactCategoryService.getImpactCategoryList())
        );
    }

    @GetMapping(API_PARAMS.GET_IMPACT_CATEGORY_BY_ID)
    public ResponseEntity<ResponseObject> getImpactCategoryById(@PathVariable long id) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_IMPACT_CATEGORY_BY_ID_SUCCESS, impactCategoryService.getImpactCategoryById(id))
        );
    }
}