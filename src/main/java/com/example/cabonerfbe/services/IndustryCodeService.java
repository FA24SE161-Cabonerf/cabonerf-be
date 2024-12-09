package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.IndustryCodeDto;
import com.example.cabonerfbe.request.IndustryCodeRequest;
import com.example.cabonerfbe.response.GetAllIndustryCodeResponse;

import java.util.List;
import java.util.UUID;

public interface IndustryCodeService {
    GetAllIndustryCodeResponse getAll(int pageCurrent, int pageSize, String keyword);
    List<IndustryCodeDto> getAllToCreateOrganization(String keyword);
    IndustryCodeDto create(IndustryCodeRequest request);
    IndustryCodeDto update(UUID id, IndustryCodeRequest request);
    IndustryCodeDto delete(UUID id);
    List<IndustryCodeDto> getInOrganization(UUID organizationId);
}
