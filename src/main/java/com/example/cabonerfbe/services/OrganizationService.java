package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.request.CreateOrganizationRequest;
import com.example.cabonerfbe.request.UpdateOrganizationRequest;
import com.example.cabonerfbe.response.GetAllOrganizationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface OrganizationService {
    GetAllOrganizationResponse getAll(int pageCurrent, int pageSize, String keyword);
    OrganizationDto createOrganization(CreateOrganizationRequest request, MultipartFile contractFile);
    OrganizationDto updateOrganization(UUID organizationId, UpdateOrganizationRequest request);
    OrganizationDto deleteOrganization(UUID organizationId);
}
