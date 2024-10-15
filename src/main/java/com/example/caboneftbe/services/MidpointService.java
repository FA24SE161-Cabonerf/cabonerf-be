package com.example.caboneftbe.services;

import com.example.caboneftbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;

import java.util.List;
import java.util.Optional;

public interface MidpointService {
    List<MidpointImpactCharacterizationFactorsDto> getAllImpactCharacterizationFactors();
    MidpointImpactCharacterizationFactorsDto getImpactCharacterizationFactorById(Long id);
}
