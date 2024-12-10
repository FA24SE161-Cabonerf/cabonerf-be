package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.IndustryCodeConverter;
import com.example.cabonerfbe.dto.IndustryCodeDto;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.IndustryCode;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.models.OrganizationIndustryCode;
import com.example.cabonerfbe.repositories.IndustryCodeRepository;
import com.example.cabonerfbe.repositories.OrganizationIndustryCodeRepository;
import com.example.cabonerfbe.repositories.OrganizationRepository;
import com.example.cabonerfbe.request.IndustryCodeRequest;
import com.example.cabonerfbe.response.GetAllIndustryCodeResponse;
import com.example.cabonerfbe.services.IndustryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IndustryCodeServiceImpl implements IndustryCodeService {

    @Autowired
    private IndustryCodeRepository icRepository;
    @Autowired
    private IndustryCodeConverter icConverter;
    @Autowired
    private OrganizationIndustryCodeRepository oicRepository;
    @Autowired
    private OrganizationRepository oRepository;

    @Override
    public GetAllIndustryCodeResponse getAll(int pageCurrent, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);

        Page<IndustryCode> industryCodePage = keyword == null ? icRepository.findAll(pageable) : icRepository.findAllByKeyword(keyword, pageable);

        int totalPage = industryCodePage.getTotalPages();

        if(pageCurrent > totalPage){
            return GetAllIndustryCodeResponse.builder()
                    .pageCurrent(1)
                    .pageSize(0)
                    .totalPage(0)
                    .industryCodes(Collections.EMPTY_LIST)
                    .build();
        }

        return GetAllIndustryCodeResponse.builder()
                .pageCurrent(pageCurrent)
                .pageSize(pageSize)
                .totalPage(totalPage)
                .industryCodes(industryCodePage.getContent().stream().map(icConverter::modelToDto).toList())
                .build();
    }

    @Override
    public List<IndustryCodeDto> getAllToCreateOrganization(String keyword) {
        List<IndustryCode> icList = keyword == null ? icRepository.findByStatus() : icRepository.findByKeyword(keyword);
        return icList.isEmpty() ? Collections.emptyList() : icList.stream().map(icConverter::modelToDto).toList();
    }

    @Override
    public IndustryCodeDto create(IndustryCodeRequest request) {

        if(icRepository.findByCode(request.getCode()).isPresent()){
            throw CustomExceptions.validator(MessageConstants.INDUSTRY_CODE_EXIST);
        }

        IndustryCode ic = new IndustryCode();
        ic.setCode(request.getCode());
        ic.setName(request.getName());

        return icConverter.modelToDto(icRepository.save(ic));
    }

    @Override
    public IndustryCodeDto update(UUID id, IndustryCodeRequest request) {
        IndustryCode ic = icRepository.findByIdWithStatus(id)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_INDUSTRY_CODE_FOUND));

        boolean isCodeDuplicate = icRepository.existsByCodeAndIdNot(request.getCode(), id);
        if (isCodeDuplicate) {
            throw CustomExceptions.badRequest(MessageConstants.INDUSTRY_CODE_EXIST);
        }

        ic.setName(request.getName());
        ic.setCode(request.getCode());

        return icConverter.modelToDto(icRepository.save(ic));
    }

    @Override
    public IndustryCodeDto delete(UUID id) {
        IndustryCode ic = icRepository.findByIdWithStatus(id)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_INDUSTRY_CODE_FOUND));

       ic.setStatus(false);

        return icConverter.modelToDto(icRepository.save(ic));
    }

    @Override
    public List<IndustryCodeDto> getInOrganization(UUID organizationId) {

        Organization o = oRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));


        return Optional.ofNullable(oicRepository.findByOrganization(organizationId))
                .filter(list -> !list.isEmpty())
                .orElseGet(() -> oicRepository.findAllByStatus())
                .stream()
                .map(OrganizationIndustryCode::getIndustryCode)
                .map(icConverter::modelToDto)
                .toList();

    }
}
