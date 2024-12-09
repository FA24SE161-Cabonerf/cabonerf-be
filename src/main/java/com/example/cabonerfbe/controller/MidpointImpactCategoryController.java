package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateMidpointImpactCategoryRequest;
import com.example.cabonerfbe.request.UpdateMidpointImpactCategoryRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.MidpointImpactCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping(API_PARAMS.CREATE_MIDPOINT_IMPACT_CATEGORY)
    private ResponseEntity<ResponseObject> create(@Valid @RequestBody CreateMidpointImpactCategoryRequest request) {
        log.info("Start create midpoint impact category. Request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create midpoint impact category success", midpointImpactCategoryService.create(request))
        );
    }

    @PutMapping(API_PARAMS.UPDATE_MIDPOINT_IMPACT_CATEGORY)
    private ResponseEntity<ResponseObject> create(@PathVariable UUID id, @Valid @RequestBody UpdateMidpointImpactCategoryRequest request) {
        log.info("Start update midpoint impact category.Id: {}, Request: {}", id, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Update midpoint impact category success", midpointImpactCategoryService.update(id, request))
        );
    }

    @DeleteMapping(API_PARAMS.DELETE_MIDPOINT_IMPACT_CATEGORY)
    private ResponseEntity<ResponseObject> create(@PathVariable UUID id) {
        log.info("Delete update midpoint impact category.Id: {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Delete midpoint impact category success", midpointImpactCategoryService.delete(id))
        );
    }

}
