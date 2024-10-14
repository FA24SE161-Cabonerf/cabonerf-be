package com.example.caboneftbe.services.impl;

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

    @Override
    public List<MidpointImpactCharacterizationFactors> getAllImpactCharacterizationFactors() {
        List<MidpointImpactCharacterizationFactors> impactCharacterizationFactors = midpointRepository.findAll();
        if (impactCharacterizationFactors.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR);
        }
        return impactCharacterizationFactors;
    }

    @Override
    public Optional<MidpointImpactCharacterizationFactors> getImpactCharacterizationFactorById(Long id) {
        Optional<MidpointImpactCharacterizationFactors> impactCharacterizationFactor = midpointRepository.findById(id);
        if (impactCharacterizationFactor.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR);
        }
        return impactCharacterizationFactor;
    }
}
