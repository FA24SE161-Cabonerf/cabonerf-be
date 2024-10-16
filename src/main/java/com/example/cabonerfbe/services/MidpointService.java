package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.response.MidpointImpactCharacterizationFactorsResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;

import java.util.List;

public interface MidpointService {
    List<MidpointImpactCharacterizationFactorsResponse> getAllMidpointFactors();
    MidpointImpactCharacterizationFactorsResponse getMidpointFactorById(Long id);

    PageList<MidpointSubstanceFactorsResponse> getAllMidpointFactorsAdmin(int currentPage, int pageSize);
}
