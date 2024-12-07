package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.MidpointImpactCharacterizationFactorsResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;

import java.util.List;
import java.util.UUID;

public interface MidpointService {
    List<MidpointImpactCharacterizationFactorsResponse> getAllMidpointFactors();
    MidpointImpactCharacterizationFactorsResponse getMidpointFactorById(UUID id);

    PageList<MidpointSubstanceFactorsResponse> getAllMidpointFactorsAdmin(PaginationRequest request);
}
