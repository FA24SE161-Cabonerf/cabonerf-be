package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.request.EmissionCompartmentRequest;
import com.example.cabonerfbe.response.EmissionCompartmentResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Emission compartment service.
 *
 * @author SonPHH.
 */
public interface EmissionCompartmentService {
    /**
     * Gets all emission compartments.
     *
     * @return the all emission compartments
     */
    List<EmissionCompartmentResponse> getAllEmissionCompartments();

    /**
     * Create method.
     *
     * @param request the request
     * @return the emission compartment dto
     */
    EmissionCompartmentDto create(EmissionCompartmentRequest request);

    /**
     * Update method.
     *
     * @param emissionCompartmentId the emission compartment id
     * @param request               the request
     * @return the emission compartment dto
     */
    EmissionCompartmentDto update(UUID emissionCompartmentId, EmissionCompartmentRequest request);

    /**
     * Delete method.
     *
     * @param emissionCompartmentId the emission compartment id
     * @return the emission compartment dto
     */
    EmissionCompartmentDto delete(UUID emissionCompartmentId);

}
