package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateOrganizationRequest;
import com.example.cabonerfbe.request.InviteUserToOrganizationRequest;
import com.example.cabonerfbe.request.UpdateOrganizationRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.OrganizationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * The class Organization controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    /**
     * Create organization method.
     *
     * @param name            the name
     * @param email           the email
     * @param description     the description
     * @param taxCode         the tax code
     * @param industryCodeIds the industry code ids
     * @param contractFile    the contract file
     * @param logo            the logo
     * @return the response entity
     */
    @PostMapping(value = API_PARAMS.MANAGER + API_PARAMS.ORGANIZATION, consumes = "multipart/form-data")
    public ResponseEntity<ResponseObject> createOrganization(@RequestParam String name,
                                                             @RequestParam @Email String email,
                                                             @RequestParam String description,
                                                             @RequestParam String taxCode,
                                                             @RequestParam List<UUID> industryCodeIds,
                                                             @RequestParam MultipartFile contractFile,
                                                             @RequestParam MultipartFile logo) {
        CreateOrganizationRequest request = new CreateOrganizationRequest(name, email, industryCodeIds, taxCode, description);
        log.info("Start createOrganization. Request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_ORGANIZATION_SUCCESS, organizationService.createOrganization(request, contractFile, logo)
        ));
    }

    /**
     * Gets all.
     *
     * @param pageCurrent the page current
     * @param pageSize    the page size
     * @param keyword     the keyword
     * @return the all
     */
    @GetMapping(API_PARAMS.MANAGER + API_PARAMS.ORGANIZATION)
    public ResponseEntity<ResponseObject> getALL(@RequestParam(required = true, defaultValue = "1") int pageCurrent,
                                                 @RequestParam(required = true, defaultValue = "5") int pageSize,
                                                 @RequestParam(required = false) String keyword) {
        log.info("Start getAllOrganization");
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_ORGANIZATION_SUCCESS, organizationService.getAll(pageCurrent, pageSize, keyword)
        ));
    }

    /**
     * Update organization method.
     *
     * @param organizationId the organization id
     * @param request        the request
     * @return the response entity
     */
    @PutMapping(API_PARAMS.MANAGER + API_PARAMS.ORGANIZATION + API_PARAMS.UPDATE_ORGANIZATION)
    public ResponseEntity<ResponseObject> updateOrganization(@PathVariable("organizationId") UUID organizationId, @RequestBody UpdateOrganizationRequest request) {
        log.info("Start updateOrganization.Id: {}, Request: {}", organizationId, request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_ORGANIZATION_SUCCESS, organizationService.updateOrganization(organizationId, request)
        ));
    }

    /**
     * Delete organization method.
     *
     * @param organizationId the organization id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.MANAGER + API_PARAMS.ORGANIZATION + API_PARAMS.DELETE_ORGANIZATION)
    public ResponseEntity<ResponseObject> deleteOrganization(@PathVariable("organizationId") UUID organizationId) {
        log.info("Start deleteOrganization.Id: {}", organizationId);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.DELETE_ORGANIZATION_SUCCESS, organizationService.deleteOrganization(organizationId)
        ));
    }

//    @PutMapping(API_PARAMS.ORGANIZATION_MANAGER + API_PARAMS.CONFIRM_CREATE_ORGANIZATION)
//    public ResponseEntity<ResponseObject> confirmCreateOrganization(@Valid @RequestParam VerifyCreateOrganizationRequest request) {

    /**
     * Invite member organization method.
     *
     * @param userId  the user id
     * @param request the request
     * @return the response entity
     */
//        log.info("Start confirmCreateOrganization. request: {}", request);
//        return ResponseEntity.ok().body(new ResponseObject(
//                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CONFIRM_CREATE_ORGANIZATION_SUCCESS, organizationService.confirm(request)
//        ));
//    }
    @PostMapping(API_PARAMS.ORGANIZATION_MANAGER + API_PARAMS.ORGANIZATION + API_PARAMS.INVITE_MEMBER_ORGANIZATION)
    public ResponseEntity<ResponseObject> inviteMemberOrganization(@RequestHeader("x-user-id") UUID userId, @Valid @RequestBody InviteUserToOrganizationRequest request) {
        log.info("Start inviteMemberOrganization. Request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Invite member organization success", organizationService.invite(userId, request))
        );
    }

    /**
     * Accept method.
     *
     * @param userOrganizationId the user organization id
     * @param token              the token
     * @return the response entity
     */
    @PutMapping(API_PARAMS.ORGANIZATION + API_PARAMS.ACCEPT_INVITE_ORGANIZATION)
    public ResponseEntity<ResponseObject> accept(@RequestParam UUID userOrganizationId, @RequestParam String token) {
        log.info("Start acceptInviteOrganization.");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Accept invite organization success", organizationService.acceptInvite(userOrganizationId, token))
        );
    }

    /**
     * Gets member in organization.
     *
     * @param organizationId the organization id
     * @return the member in organization
     */
    @GetMapping(API_PARAMS.ORGANIZATION + API_PARAMS.GET_MEMBER_IN_ORGANIZATION)
    public ResponseEntity<ResponseObject> getMemberInOrganization(@PathVariable UUID organizationId) {
        log.info("Start getMemberInOrganization. organizationId: {}", organizationId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get member organization success", organizationService.getMemberInOrganization(organizationId))
        );
    }

    /**
     * Remove member method.
     *
     * @param userId             the user id
     * @param userOrganizationId the user organization id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.ORGANIZATION_MANAGER + API_PARAMS.ORGANIZATION + API_PARAMS.REMOVE_MEMBER_IN_ORGANIZATION)
    public ResponseEntity<ResponseObject> removeMember(@RequestHeader("x-user-id") UUID userId, @PathVariable("userOrganizationId") UUID userOrganizationId) {
        log.info("Start removeMember");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Remove member success", organizationService.removeMember(userId, userOrganizationId))
        );
    }

    /**
     * Upload logo method.
     *
     * @param organizationId the organization id
     * @param logo           the logo
     * @return the response entity
     */
    @PostMapping(API_PARAMS.ORGANIZATION + API_PARAMS.UPLOAD_LOGO)
    public ResponseEntity<ResponseObject> uploadLogo(@PathVariable("organizationId") UUID organizationId, @RequestParam("logo") MultipartFile logo) {
        log.info("Start uploadLogoOrganization. Id: {}", organizationId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Upload logo success", organizationService.uploadLogo(organizationId, logo))
        );
    }

    /**
     * Gets all by user.
     *
     * @param userId the user id
     * @return the all by user
     */
    @GetMapping(API_PARAMS.ORGANIZATION)
    public ResponseEntity<ResponseObject> getAllByUser(@RequestHeader("x-user-id") UUID userId) {
        log.info("Start getAllOrganizationByUser. userId: {}", userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get all organization by user success", organizationService.getByUser(userId))
        );
    }

    /**
     * Gets organization by id.
     *
     * @param organizationId the organization id
     * @return the organization by id
     */
    @GetMapping(API_PARAMS.ORGANIZATION + API_PARAMS.GET_ORGANIZATION_BY_ID)
    public ResponseEntity<ResponseObject> getOrganizationById(@PathVariable UUID organizationId) {
        log.info("Start getOrganizationById. Id: {}", organizationId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get organization by id success", organizationService.getOrganizationById(organizationId))
        );
    }

    /**
     * Leave organization method.
     *
     * @param userId             the user id
     * @param userOrganizationId the user organization id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.ORGANIZATION + API_PARAMS.OUT_ORGANIZATION)
    public ResponseEntity<ResponseObject> leaveOrganization(@RequestHeader("x-user-id") UUID userId, @PathVariable UUID userOrganizationId) {
        log.info("Start leaveOrganization. userId: {}", userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Leave organization success", organizationService.leaveOrganization(userId, userOrganizationId))
        );
    }
}
