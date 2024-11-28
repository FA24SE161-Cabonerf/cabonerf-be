package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.OrganizationService;
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
    public ResponseEntity<ResponseObject> createOrganization(@RequestParam String name, @RequestParam String email, @RequestParam MultipartFile contractFile) {
        CreateOrganizationRequest request = new CreateOrganizationRequest(name, email);
        log.info("Start createOrganization. Request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_ORGANIZATION_SUCCESS, organizationService.createOrganization(request, contractFile)
        ));
    }

    @GetMapping(API_PARAMS.MANAGER)
    public ResponseEntity<ResponseObject> getALL(@RequestParam(required = true, defaultValue = "1") int pageCurrent,
                                                 @RequestParam(required = true, defaultValue = "5") int pageSize,
                                                 @RequestParam(required = false) String keyword) {
        log.info("Start getAllOrganization");
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_ORGANIZATION_SUCCESS, organizationService.getAll(pageCurrent, pageSize, keyword)
        ));
    }

    @PutMapping(API_PARAMS.MANAGER + API_PARAMS.UPDATE_ORGANIZATION)
    public ResponseEntity<ResponseObject> updateOrganization(@PathVariable("organizationId") UUID organizationId, @RequestBody UpdateOrganizationRequest request) {
        log.info("Start updateOrganization.Id: {}, Request: {}", organizationId, request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_ORGANIZATION_SUCCESS, organizationService.updateOrganization(organizationId, request)
        ));
    }

    @DeleteMapping(API_PARAMS.MANAGER + API_PARAMS.DELETE_ORGANIZATION)
    public ResponseEntity<ResponseObject> deleteOrganization(@PathVariable("organizationId") UUID organizationId) {
        log.info("Start deleteOrganization.Id: {}", organizationId);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.DELETE_ORGANIZATION_SUCCESS, organizationService.deleteOrganization(organizationId)
        ));
    }

    @PutMapping(API_PARAMS.ORGANIZATION_MANAGER + API_PARAMS.CONFIRM_CREATE_ORGANIZATION)
    public ResponseEntity<ResponseObject> confirmCreateOrganization(@RequestParam("organizationId") UUID organizationId, @RequestParam("token") String token) {
        VerifyCreateOrganizationRequest request = new VerifyCreateOrganizationRequest(organizationId, token);
        log.info("Start confirmCreateOrganization. request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CONFIRM_CREATE_ORGANIZATION_SUCCESS, organizationService.confirm(request)
        ));
    }

    @PostMapping(API_PARAMS.ORGANIZATION_MANAGER + API_PARAMS.INVITE_MEMBER_ORGANIZATION)
    public ResponseEntity<ResponseObject> inviteMemberOrganization(@RequestHeader("x-user-id") UUID userId, @Valid @RequestBody InviteUserToOrganizationRequest request) {
        log.info("Start inviteMemberOrganization. Request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Invite member organization success", organizationService.invite(userId, request))
        );
    }

    @PutMapping(API_PARAMS.ACCEPT_INVITE_ORGANIZATION)
    public ResponseEntity<ResponseObject> accept(@RequestHeader("x-user-id") UUID userId, @Valid @RequestBody AcceptInviteRequest request) {
        log.info("Start acceptInviteOrganization. userId: {}, request: {}", userId, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Accept invite organization success", organizationService.acceptDenyInvite(userId, request, "Accept"))
        );
    }

    @PutMapping(API_PARAMS.DENY_INVITE_ORGANIZATION)
    public ResponseEntity<ResponseObject> deny(@RequestHeader("x-user-id") UUID userId, @Valid @RequestBody AcceptInviteRequest request) {
        log.info("Start denyInviteOrganization. userId: {}, request: {}", userId, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Deny invite organization success", organizationService.acceptDenyInvite(userId, request, "Deny"))
        );
    }

    @GetMapping(API_PARAMS.GET_MEMBER_IN_ORGANIZATION)
    public ResponseEntity<ResponseObject> getMemberInOrganization(@PathVariable UUID organizationId) {
        log.info("Start getMemberInOrganization. organizationId: {}",organizationId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get member organization success", organizationService.getMemberInOrganization(organizationId))
        );
    }

    @DeleteMapping(API_PARAMS.ORGANIZATION_MANAGER + API_PARAMS.REMOVE_MEMBER_IN_ORGANIZATION)
    public ResponseEntity<ResponseObject> removeMember(@RequestHeader("x-user-id") UUID userId, @PathVariable("userOrganizationId") UUID userOrganizationId) {
        log.info("Start removeMember");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Remove member success", organizationService.removeMember(userId, userOrganizationId))
        );
    }

    @GetMapping(API_PARAMS.GET_LIST_INVITE_BY_USER)
    public ResponseEntity<ResponseObject> getListInvite(@RequestHeader("x-user-id") UUID userId) {
        log.info("Start getListInvite");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get list invite success", organizationService.getListInviteByUser(userId))
        );
    }

    @PutMapping(API_PARAMS.UPLOAD_LOGO)
    public ResponseEntity<ResponseObject> uploadLogo(@PathVariable("organizationId") UUID organizationId, @RequestParam("logo") MultipartFile logo){
        log.info("Start uploadLogoOrganization. Id: {}",organizationId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Upload logo success", organizationService.uploadLogo(organizationId,logo))
        );
    }

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllByUser(@RequestHeader("x-user-id") UUID userId){
        log.info("Start getAllOrganizationByUser. userId: {}",userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Get all organization by user success",organizationService.getByUser(userId))
        );
    }

    @GetMapping(API_PARAMS.GET_ORGANIZATION_BY_ID)
    public ResponseEntity<ResponseObject> getOrganizationById(@PathVariable UUID organizationId){
        log.info("Start getOrganizationById. Id: {}", organizationId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Get organization by id success",organizationService.getOrganizationById(organizationId))
        );
    }
}
