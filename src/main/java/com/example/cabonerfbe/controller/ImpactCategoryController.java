package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.BaseImpactCategoryRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ImpactCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.IMPACT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ImpactCategoryController {
    @Autowired
    private ImpactCategoryService impactCategoryService;

    @GetMapping(API_PARAMS.GET_ALL_IMPACT_CATEGORIES)
    public ResponseEntity<ResponseObject> getImpactCategoryList() {
        log.info("Start getImpactCategoryList");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_IMPACT_CATEGORIES_SUCCESS, impactCategoryService.getImpactCategoryList())
        );
    }

    @GetMapping(API_PARAMS.GET_IMPACT_CATEGORY_BY_ID)
    public ResponseEntity<ResponseObject> getImpactCategoryById(@PathVariable long categoryId) {
        log.info("Start getImpactCategoryById id: {}", categoryId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_IMPACT_CATEGORY_BY_ID_SUCCESS, impactCategoryService.getImpactCategoryById(categoryId))
        );
    }

    @PostMapping(API_PARAMS.CREATE_IMPACT_CATEGORY_FOR_IMPACT_METHOD)
    public ResponseEntity<ResponseObject> createImpactCategoryForImpactMethod(@PathVariable long methodId, @Valid @RequestBody BaseImpactCategoryRequest request) {
        log.info("Start createImpactCategoryForImpactMethod methodId: {}", methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_IMPACT_CATEGORY_FOR_IMPACT_METHOD_SUCCESS, impactCategoryService.createImpactCategoryForImpactMethod(methodId, request))
        );
    }


}
