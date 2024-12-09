package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.EmissionCompartmentConverter;
import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.EmissionCompartment;
import com.example.cabonerfbe.repositories.EmissionCompartmentRepository;
import com.example.cabonerfbe.request.EmissionCompartmentRequest;
import com.example.cabonerfbe.response.EmissionCompartmentResponse;
import com.example.cabonerfbe.services.EmissionCompartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmissionCompartmentServiceImpl implements EmissionCompartmentService {
    @Autowired
    EmissionCompartmentRepository emissionCompartmentRepository;
    @Autowired
    EmissionCompartmentConverter emissionCompartmentConverter;

    @Override
    public List<EmissionCompartmentResponse> getAllEmissionCompartments() {
        List<EmissionCompartment> emissionCompartmentList = emissionCompartmentRepository.findByStatus(Constants.STATUS_TRUE);
        if (emissionCompartmentList.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_EMISSION_COMPARTMENT_FOUND);
        }
        return emissionCompartmentConverter.fromListEmissionCompartmentToListEmissionCompartmentResponse(emissionCompartmentList);
    }

    @Override
    public EmissionCompartmentDto create(EmissionCompartmentRequest request) {
        EmissionCompartment ec = new EmissionCompartment();
        ec.setName(request.getName());
        ec.setDescription(request.getDescription());
        return emissionCompartmentConverter.fromEmissionCompartmentToEmissionCompartmentDto(emissionCompartmentRepository.save(ec));
    }

    @Override
    public EmissionCompartmentDto update(UUID emissionCompartmentId, EmissionCompartmentRequest request) {
        EmissionCompartment ec = emissionCompartmentRepository.findByIdAndStatus(emissionCompartmentId, true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_EMISSION_COMPARTMENT_FOUND));
        ec.setName(request.getName());
        if (!request.getDescription().isEmpty()) {
            ec.setDescription(request.getDescription());
        }
        return emissionCompartmentConverter.fromEmissionCompartmentToEmissionCompartmentDto(emissionCompartmentRepository.save(ec));
    }

    @Override
    public EmissionCompartmentDto delete(UUID emissionCompartmentId) {
        EmissionCompartment ec = emissionCompartmentRepository.findByIdAndStatus(emissionCompartmentId, true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_EMISSION_COMPARTMENT_FOUND));
        ec.setStatus(false);
        return emissionCompartmentConverter.fromEmissionCompartmentToEmissionCompartmentDto(emissionCompartmentRepository.save(ec));
    }
}
