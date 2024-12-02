package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.IndustryCodeConverter;
import com.example.cabonerfbe.dto.IndustryCodeDto;
import com.example.cabonerfbe.repositories.IndustryCodeRepository;
import com.example.cabonerfbe.repositories.OrganizationIndustryCodeRepository;
import com.example.cabonerfbe.request.IndustryCodeRequest;
import com.example.cabonerfbe.response.GetAllIndustryCodeResponse;
import com.example.cabonerfbe.services.IndustryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IndustryCodeServiceImpl implements IndustryCodeService {

    @Autowired
    private IndustryCodeRepository icRepository;
    @Autowired
    private IndustryCodeConverter icConverter;
    @Autowired
    private OrganizationIndustryCodeRepository oicRepository;

    @Override
    public GetAllIndustryCodeResponse getAll(int pageCurrent, int pageSize, String keyword) {
        return null;
    }

    @Override
    public IndustryCodeDto create(IndustryCodeRequest request) {
        return null;
    }

    @Override
    public IndustryCodeDto update(UUID id, IndustryCodeRequest request) {
        return null;
    }

    @Override
    public IndustryCodeDto delete(UUID id) {
        return null;
    }

    @Override
    public List<IndustryCodeDto> getInOrganization(UUID organizationId) {
        return null;
    }
}
