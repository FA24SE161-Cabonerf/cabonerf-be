package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.GetAllOrganizationResponse;
import com.example.cabonerfbe.response.InviteMemberResponse;
import com.example.cabonerfbe.response.LoginResponse;
import com.example.cabonerfbe.response.UploadOrgLogoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    GetAllOrganizationResponse getAll(int pageCurrent, int pageSize, String keyword);

    CreateOrganizationDto createOrganization(CreateOrganizationRequest request, MultipartFile contractFile, MultipartFile logo);

    OrganizationDto updateOrganization(UUID organizationId, UpdateOrganizationRequest request);

    OrganizationDto deleteOrganization(UUID organizationId);

//    LoginResponse confirm(VerifyCreateOrganizationRequest request);

    List<InviteUserOrganizationDto> invite(UUID userId, InviteUserToOrganizationRequest request);

    LoginResponse acceptInvite(UUID organizationId, String token);

    List<String> removeMember(UUID userId, UUID userOrganizationId);

    List<String> leaveOrganization(UUID userId, UUID userOrganizationId);

    List<InviteUserOrganizationDto> getMemberInOrganization(UUID organizationId);

    UploadOrgLogoResponse uploadLogo(UUID organizationId, MultipartFile logo);

    List<GetOrganizationByUserDto> getByUser(UUID userId);

    OrganizationDto getOrganizationById(UUID organizationId);
}
