package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.enums.MessageConstants;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.ImpactMethodService;
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
public class ImpactMethodController {
    @Autowired
    private ImpactMethodService impactMethodService;

    @GetMapping(API_PARAMS.GET_ALL_IMPACT_METHODS)
    public ResponseEntity<ResponseObject> getAllImpactMethods() {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_IMPACT_METHODS_SUCCESS, impactMethodService.getAllImpactMethods())
        );
    }

    @GetMapping(API_PARAMS.GET_IMPACT_METHOD_BY_ID)
    public ResponseEntity<ResponseObject> getImpactMethodById(@PathVariable long id) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_IMPACT_METHOD_BY_ID_SUCCESS, impactMethodService.getImpactMethodById(id))
        );
    }

    @GetMapping(API_PARAMS.GET_CATEGORY_BY_METHOD_ID)
    public ResponseEntity<ResponseObject> getCategoryByMethodId(@PathVariable long id) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_CATEGORY_BY_METHOD_ID_SUCCESS, impactMethodService.getCategoryByMethodId(id))
        );
    }
}
