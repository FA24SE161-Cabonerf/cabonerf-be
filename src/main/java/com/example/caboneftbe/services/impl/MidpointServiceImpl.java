package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.caboneftbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.caboneftbe.enums.MessageConstants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;
import com.example.caboneftbe.repositories.MidpointRepository;
import com.example.caboneftbe.services.MidpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
