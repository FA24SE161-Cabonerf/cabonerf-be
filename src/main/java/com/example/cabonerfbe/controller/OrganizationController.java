package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.models.Contract;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.request.CreateOrganizationRequest;
import com.example.cabonerfbe.request.RequestObject;
import com.example.cabonerfbe.request.UpdateOrganizationRequest;
import com.example.cabonerfbe.request.VerifyCreateOrganizationRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.OrganizationService;
import io.swagger.v3.oas.annotations.headers.Header;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.ORGANIZATION)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping(API_PARAMS.MANAGER)
    public ResponseEntity<ResponseObject> createOrganization(@RequestParam String name,@RequestParam String email, @RequestParam MultipartFile contractFile) {
        CreateOrganizationRequest request = new CreateOrganizationRequest(name, email);
        log.info("Start createOrganization. Request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_ORGANIZATION_SUCCESS,organizationService.createOrganization(request,contractFile)
        ));
    }

    @GetMapping(API_PARAMS.MANAGER)
    public ResponseEntity<ResponseObject> getALL(@RequestParam(required = true, defaultValue = "1") int pageCurrent,
                                                 @RequestParam(required = true, defaultValue = "5") int pageSize,
                                                 @RequestParam(required = false) String keyword){
        log.info("Start getAllOrganization");
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,MessageConstants.GET_ALL_ORGANIZATION_SUCCESS,organizationService.getAll(pageCurrent, pageSize, keyword)
        ));
    }

    @PutMapping(API_PARAMS.MANAGER + API_PARAMS.UPDATE_ORGANIZATION)
    public ResponseEntity<ResponseObject> updateOrganization(@PathVariable("organizationId") UUID organizationId, @RequestBody UpdateOrganizationRequest request) {
        log.info("Start updateOrganization.Id: {}, Request: {}",organizationId, request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,MessageConstants.UPDATE_ORGANIZATION_SUCCESS,organizationService.updateOrganization(organizationId,request)
        ));
    }

    @DeleteMapping(API_PARAMS.MANAGER + API_PARAMS.DELETE_ORGANIZATION)
    public ResponseEntity<ResponseObject> deleteOrganization(@PathVariable("organizationId") UUID organizationId) {
        log.info("Start deleteOrganization.Id: {}",organizationId);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,MessageConstants.DELETE_ORGANIZATION_SUCCESS,organizationService.deleteOrganization(organizationId)
        ));
    }

    @DeleteMapping(API_PARAMS.MANAGER + API_PARAMS.CONFIRM_CREATE_ORGANIZATION)
    public ResponseEntity<ResponseObject> confirmCreateOrganization(@RequestHeader("x-user-id") UUID userId, @Valid @RequestBody VerifyCreateOrganizationRequest request) {
        log.info("Start confirmCreateOrganization. UserId: {}, request: {}", userId,request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,MessageConstants.CONFIRM_CREATE_ORGANIZATION_SUCCESS,organizationService.confirm(userId,request)
        ));
    }

}
