package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointSubstanceFactorsDto;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import com.example.cabonerfbe.repositories.MidpointRepository;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.MidpointImpactCharacterizationFactorsResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import com.example.cabonerfbe.services.MidpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MidpointServiceImpl implements MidpointService {
    @Autowired
    private MidpointRepository midpointRepository;

    @Autowired
    private MidpointImpactCharacterizationFactorConverter midpointConverter;

    private static final int PAGE_INDEX_ADJUSTMENT = 1;


    @Override
    public List<MidpointImpactCharacterizationFactorsResponse> getAllMidpointFactors() {
        List<MidpointImpactCharacterizationFactors> impactCharacterizationFactors = midpointRepository.findAllByStatus(Constants.STATUS_TRUE);
        if (impactCharacterizationFactors.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR);
        }

        return midpointConverter.fromMidpointDtoListToMidpointResponseList(midpointConverter.fromMidpointListToMidpointDtoList(impactCharacterizationFactors));

    }

    @Override
    public MidpointImpactCharacterizationFactorsResponse getMidpointFactorById(Long id) {
        MidpointImpactCharacterizationFactors impactCharacterizationFactor = midpointRepository.findById(id).orElseThrow(()
                -> CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR)
        );

        return midpointConverter.fromMidpointDtoToMidpointResponse(midpointConverter.fromMidpointToMidpointDto(impactCharacterizationFactor));
    }

    @Override
    public PageList<MidpointSubstanceFactorsResponse> getAllMidpointFactorsAdmin(PaginationRequest request) {
        int pageSize = request.getPageSize();
        int currentPage = request.getCurrentPage();

        Pageable pageable = PageRequest.of(currentPage - PAGE_INDEX_ADJUSTMENT, pageSize);

        Page<Object[]> midpointSubstanceFactorPage = midpointRepository.findAllWithPerspective(pageable);
        long totalRecords = midpointSubstanceFactorPage.getTotalElements();

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        if (currentPage > totalPages) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("currentPage", MessageConstants.CURRENT_PAGE_EXCEED_TOTAL_PAGES));
        }

        List<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoList = midpointSubstanceFactorPage.getContent().stream()
                .map(midpointConverter::fromQueryResultsToDto)
                .collect(Collectors.toList());
        PageList<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoPageList = new PageList<>();
        midpointSubstanceFactorsDtoPageList.setCurrentPage(currentPage);
        midpointSubstanceFactorsDtoPageList.setTotalPage(totalPages);
        midpointSubstanceFactorsDtoPageList.setListResult(midpointSubstanceFactorsDtoList);
        return midpointConverter.fromMidpointSubstanceFactorPageListDtoToMidpointSubstanceFactorPageListResponse(midpointSubstanceFactorsDtoPageList);
    }
}