package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;
import com.example.cabonerfbe.services.ExchangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExchangesServiceImpl implements ExchangesService {

    @Autowired
    ProcessRepository processRepository;
    @Autowired
    ExchangesRepository exchangesRepository;
    @Autowired
    EmissionSubstancesRepository emissionSubstancesRepository;
    @Autowired
    ExchangesTypeRepository exchangesTypeRepository;
    @Autowired
    MidpointImpactCharacterizationFactorsRepository midpointImpactCharacterizationFactorsRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    ProcessConverter processConverter;
    @Autowired
    ExchangesConverter exchangesConverter;

    @Override
    public CreateProcessResponse createElementaryExchanges(CreateElementaryRequest request) {
        return null;
    }

    @Override
    public CreateProcessResponse createProductExchanges(CreateProductRequest request) {
        return null;
    }
}
