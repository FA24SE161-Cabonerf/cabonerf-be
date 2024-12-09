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

import java.util.UUID;

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
    public ResponseEntity<ResponseObject> getImpactCategoryById(@PathVariable UUID categoryId) {
        log.info("Start getImpactCategoryById id: {}", categoryId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_IMPACT_CATEGORY_BY_ID_SUCCESS, impactCategoryService.getImpactCategoryById(categoryId))
        );
    }

    @PostMapping(API_PARAMS.CREATE_IMPACT_CATEGORY)
    public ResponseEntity<ResponseObject> createImpactCategory(@Valid @RequestBody BaseImpactCategoryRequest request) {
        log.info("Start createImpactCategory. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_IMPACT_CATEGORY_SUCCESS, impactCategoryService.createImpactCategory(request))
        );
    }

    @PutMapping(API_PARAMS.UPDATE_IMPACT_CATEGORY_BY_ID)
    public ResponseEntity<ResponseObject> updateImpactCategoryById(@PathVariable UUID categoryId, @Valid @RequestBody BaseImpactCategoryRequest request) {
        log.info("Start updateImpactCategoryById. id: {}, request: {}", categoryId, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_IMPACT_CATEGORY_SUCCESS, impactCategoryService.updateImpactCategoryById(categoryId, request))
        );
    }

    @DeleteMapping(API_PARAMS.DELETE_IMPACT_CATEGORY_BY_ID)
    public ResponseEntity<ResponseObject> deleteImpactCategoryById(@PathVariable UUID categoryId) {
        log.info("Start deleteImpactCategoryById. id: {}", categoryId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.DELETE_IMPACT_CATEGORY_SUCCESS, impactCategoryService.deleteImpactCategoryById(categoryId))
        );
    }

    @GetMapping(API_PARAMS.GET_METHOD_BY_IMPACT_CATEGORY_ID)
    public ResponseEntity<ResponseObject> getMethodWithCategoryId(@PathVariable UUID categoryId) {
        log.info("Start getMethodWithCategoryId. id: {}", categoryId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_METHOD_BY_IMPACT_CATEGORY_SUCCESS, impactCategoryService.getMethodByImpactCategoryId(categoryId))
        );
    }

    @DeleteMapping(API_PARAMS.DELETE_IMPACT_CATEGORY_IN_METHOD)
    public ResponseEntity<ResponseObject> deleteImpactCategoryFromMethod(@PathVariable UUID categoryId, @PathVariable UUID methodId) {
        log.info("Start deleteImpactCategoryFromMethod. categoryId: {}, methodId: {}", categoryId, methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.DELETE_CATEGORY_FROM_METHOD_SUCCESS, impactCategoryService.deleteCategoryFromMethod(categoryId, methodId))
        );
    }

}
