package com.example.caboneftbe.services;

import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;

import java.util.List;
import java.util.Optional;

public interface MidpointService {
    List<MidpointImpactCharacterizationFactors> getAllImpactCharacterizationFactors();
    Optional<MidpointImpactCharacterizationFactors> getImpactCharacterizationFactorById(Long id);
}
