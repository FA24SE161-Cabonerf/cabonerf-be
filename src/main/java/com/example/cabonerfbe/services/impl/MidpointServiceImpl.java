package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import com.example.cabonerfbe.repositories.MidpointRepository;
import com.example.cabonerfbe.services.MidpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MidpointServiceImpl implements MidpointService {
    @Autowired
    private MidpointRepository midpointRepository;

    @Autowired
    private MidpointImpactCharacterizationFactorConverter midpointConverter;

    @Override
    public List<MidpointImpactCharacterizationFactorsDto> getAllImpactCharacterizationFactors() {
        List<MidpointImpactCharacterizationFactors> impactCharacterizationFactors = midpointRepository.findAll();
        if (impactCharacterizationFactors.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR);
        }


        return midpointConverter.fromMidpointListToMidpointDtoList(impactCharacterizationFactors);

    }

    @Override
    public MidpointImpactCharacterizationFactorsDto getImpactCharacterizationFactorById(Long id) {
        MidpointImpactCharacterizationFactors impactCharacterizationFactor = midpointRepository.findById(id).orElseThrow(()
                -> CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR)
        );

        return midpointConverter.fromMidpointImpactCharacterizationFactorsToMidpointImpactCharacterizationFactorsDto(impactCharacterizationFactor);
    }
}
