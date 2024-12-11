package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.MidpointSubstanceFactorsDto;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.request.CreateFactorRequest;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.EmissionSubstanceDashboardResponse;
import com.example.cabonerfbe.response.MidpointImpactCharacterizationFactorsResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Midpoint service.
 *
 * @author SonPHH.
 */
public interface MidpointService {
    /**
     * Gets all midpoint factors.
     *
     * @return the all midpoint factors
     */
    List<MidpointImpactCharacterizationFactorsResponse> getAllMidpointFactors();

    /**
     * Gets midpoint factor by id.
     *
     * @param id the id
     * @return the midpoint factor by id
     */
    MidpointImpactCharacterizationFactorsResponse getMidpointFactorById(UUID id);

    /**
     * Gets all midpoint factors admin.
     *
     * @param request       the request
     * @param compartmentId the compartment id
     * @param keyword       the keyword
     * @return the all midpoint factors admin
     */
    PageList<MidpointSubstanceFactorsResponse> getAllMidpointFactorsAdmin(PaginationRequest request, UUID compartmentId, String keyword);

    /**
     * Create method.
     *
     * @param request the request
     * @return the list
     */
    List<MidpointSubstanceFactorsDto> create(CreateFactorRequest request);

    /**
     * Delete method.
     *
     * @param id the id
     * @return the list
     */
    List<MidpointSubstanceFactorsDto> delete(UUID id);

    /**
     * Gets emission substance dashboard.
     *
     * @return the emission substance dashboard
     */
    List<EmissionSubstanceDashboardResponse> getEmissionSubstanceDashboard();
}
