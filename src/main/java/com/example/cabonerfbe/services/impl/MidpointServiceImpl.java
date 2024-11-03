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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MidpointServiceImpl implements MidpointService {
    @Autowired
    private MidpointRepository midpointRepository;

    @Autowired
    private MidpointImpactCharacterizationFactorConverter midpointConverter;

    private static final int PAGE_INDEX_ADJUSTMENT = 1;


    @Override
    public List<MidpointImpactCharacterizationFactorsResponse> getAllMidpointFactors() {
        List<MidpointImpactCharacterizationFactors> factors  = midpointRepository.findAllByStatus(Constants.STATUS_TRUE);
        return midpointConverter.fromMidpointListToMidpointResponseList(factors);

    }

    @Override
    public MidpointImpactCharacterizationFactorsResponse getMidpointFactorById(UUID id) {
        MidpointImpactCharacterizationFactors factor  = midpointRepository.findByIdAndStatus(id, Constants.STATUS_TRUE).orElseThrow(()
                -> CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR)
        );
        return midpointConverter.fromMidpointToMidpointResponse(factor);
    }

    @Override
    public PageList<MidpointSubstanceFactorsResponse> getAllMidpointFactorsAdmin(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getCurrentPage() - PAGE_INDEX_ADJUSTMENT, request.getPageSize());

        Page<Object[]> midpointSubstanceFactorPage = midpointRepository.findAllWithPerspective(pageable);
        int totalPages = midpointSubstanceFactorPage.getTotalPages();

        if (request.getCurrentPage() > totalPages) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("currentPage", MessageConstants.CURRENT_PAGE_EXCEED_TOTAL_PAGES));
        }

        // cái này có thể viết thành 1 converter List<> to List<> thôi. như nhau th nhma viết thế này ngầu vl =))
        // cái nữa là dùng converter line-by-line sẽ ổn định về performance hơn là tạo 1 stream.
        List<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoList = midpointSubstanceFactorPage.getContent().stream()
                .map(midpointConverter::fromQueryResultsToDto)
                .collect(Collectors.toList());

        PageList<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoPageList = new PageList<>();
        midpointSubstanceFactorsDtoPageList.setCurrentPage(request.getCurrentPage());
        midpointSubstanceFactorsDtoPageList.setTotalPage(totalPages);
        midpointSubstanceFactorsDtoPageList.setListResult(midpointSubstanceFactorsDtoList);
        return midpointConverter.fromMidpointSubstanceFactorPageListDtoToMidpointSubstanceFactorPageListResponse(midpointSubstanceFactorsDtoPageList);
    }
}
