package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.CreateOrganizationDto;
import com.example.cabonerfbe.dto.GetOrganizationByUserDto;
import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.request.CreateOrganizationRequest;
import com.example.cabonerfbe.request.InviteUserToOrganizationRequest;
import com.example.cabonerfbe.request.UpdateOrganizationRequest;
import com.example.cabonerfbe.response.GetAllOrganizationResponse;
import com.example.cabonerfbe.response.LoginResponse;
import com.example.cabonerfbe.response.UploadOrgLogoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * The interface Organization service.
 *
 * @author SonPHH.
 */
public interface OrganizationService {
    /**
     * Gets all.
     *
     * @param pageCurrent the page current
     * @param pageSize    the page size
     * @param keyword     the keyword
     * @return the all
     */
    GetAllOrganizationResponse getAll(int pageCurrent, int pageSize, String keyword);

    /**
     * Create organization method.
     *
     * @param request      the request
     * @param contractFile the contract file
     * @param logo         the logo
     * @return the create organization dto
     */
    CreateOrganizationDto createOrganization(CreateOrganizationRequest request, MultipartFile contractFile, MultipartFile logo);

    /**
     * Update organization method.
     *
     * @param organizationId the organization id
     * @param request        the request
     * @return the organization dto
     */
    OrganizationDto updateOrganization(UUID organizationId, UpdateOrganizationRequest request);

    /**
     * Delete organization method.
     *
     * @param organizationId the organization id
     * @return the organization dto
     */
    OrganizationDto deleteOrganization(UUID organizationId);

//    LoginResponse confirm(VerifyCreateOrganizationRequest request);

    /**
     * Invite method.
     *
     * @param userId  the user id
     * @param request the request
     * @return the list
     */
    List<InviteUserOrganizationDto> invite(UUID userId, InviteUserToOrganizationRequest request);

    /**
     * Accept invite method.
     *
     * @param organizationId the organization id
     * @param token          the token
     * @return the login response
     */
    LoginResponse acceptInvite(UUID organizationId, String token);

    /**
     * Remove member method.
     *
     * @param userId             the user id
     * @param userOrganizationId the user organization id
     * @return the list
     */
    List<String> removeMember(UUID userId, UUID userOrganizationId);

    /**
     * Leave organization method.
     *
     * @param userId             the user id
     * @param userOrganizationId the user organization id
     * @return the list
     */
    List<String> leaveOrganization(UUID userId, UUID userOrganizationId);

    /**
     * Gets member in organization.
     *
     * @param organizationId the organization id
     * @return the member in organization
     */
    List<InviteUserOrganizationDto> getMemberInOrganization(UUID organizationId);

    /**
     * Upload logo method.
     *
     * @param organizationId the organization id
     * @param logo           the logo
     * @return the upload org logo response
     */
    UploadOrgLogoResponse uploadLogo(UUID organizationId, MultipartFile logo);

    /**
     * Gets by user.
     *
     * @param userId the user id
     * @return the by user
     */
    List<GetOrganizationByUserDto> getByUser(UUID userId);

    /**
     * Gets organization by id.
     *
     * @param organizationId the organization id
     * @return the organization by id
     */
    OrganizationDto getOrganizationById(UUID organizationId);
}
