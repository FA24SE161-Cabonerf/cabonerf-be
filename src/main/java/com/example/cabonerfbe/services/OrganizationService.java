package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
import com.example.cabonerfbe.dto.MemberOrganizationDto;
import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.dto.UserOrganizationDto;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.GetAllOrganizationResponse;
import com.example.cabonerfbe.response.GetInviteListResponse;
import com.example.cabonerfbe.response.InviteMemberResponse;
import com.example.cabonerfbe.response.LoginResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    GetAllOrganizationResponse getAll(int pageCurrent, int pageSize, String keyword);
    OrganizationDto createOrganization(CreateOrganizationRequest request, MultipartFile contractFile);
    OrganizationDto updateOrganization(UUID organizationId, UpdateOrganizationRequest request);
    OrganizationDto deleteOrganization(UUID organizationId);
    LoginResponse confirm(VerifyCreateOrganizationRequest request);
    InviteMemberResponse invite(UUID userId, InviteUserToOrganizationRequest request);
    UserOrganizationDto acceptDenyInvite(UUID userId, AcceptInviteRequest request,String action);
    InviteUserOrganizationDto removeMember(UUID userId,UUID userOrganizationId);
    List<MemberOrganizationDto> getMemberInOrganization(UUID userId);
    List<UserOrganizationDto> getListInviteByUser(UUID userId);
}
