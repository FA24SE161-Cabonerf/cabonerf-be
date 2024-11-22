package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.request.EmissionCompartmentRequest;
import com.example.cabonerfbe.response.EmissionCompartmentResponse;

import java.util.List;
import java.util.UUID;

public interface EmissionCompartmentService {
    List<EmissionCompartmentResponse> getAllEmissionCompartments();
    EmissionCompartmentDto create(EmissionCompartmentRequest request);
    EmissionCompartmentDto update(UUID emissionCompartmentId, EmissionCompartmentRequest request);
    EmissionCompartmentDto delete(UUID emissionCompartmentId);

}
