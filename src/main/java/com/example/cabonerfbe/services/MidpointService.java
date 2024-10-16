package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.MidpointImpactCharacterizationFactorsDto;

import java.util.List;

public interface MidpointService {
    List<MidpointImpactCharacterizationFactorsDto> getAllImpactCharacterizationFactors();
    MidpointImpactCharacterizationFactorsDto getImpactCharacterizationFactorById(Long id);
}
