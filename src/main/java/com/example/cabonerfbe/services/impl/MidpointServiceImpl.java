package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointSubstanceFactorsDto;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import com.example.cabonerfbe.repositories.MidpointRepository;
import com.example.cabonerfbe.response.MidpointImpactCharacterizationFactorsResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import com.example.cabonerfbe.services.MidpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MidpointServiceImpl implements MidpointService {
    @Autowired
    private MidpointRepository midpointRepository;

    @Autowired
    private MidpointImpactCharacterizationFactorConverter midpointConverter;

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
    public PageList<MidpointSubstanceFactorsResponse> getAllMidpointFactorsAdmin(int currentPage, int pageSize) {
        List<Object[]> midpointSubstanceFactorList = midpointRepository.findAllWithPerspective(PageRequest.of(1, 20));
        List<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoList = new ArrayList<>();
        for (Object[] midpointSubstance : midpointSubstanceFactorList) {
            midpointSubstanceFactorsDtoList.add(midpointConverter.fromQueryResultsToDto(midpointSubstance));
        }
        PageList<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoPageList = new PageList<>();
        midpointSubstanceFactorsDtoPageList.setListResult(midpointSubstanceFactorsDtoList);
        return midpointConverter.fromMidpointSubstanceFactorPageListDtoToMidpointSubstanceFactorPageListResponse(midpointSubstanceFactorsDtoPageList);
    }
}
