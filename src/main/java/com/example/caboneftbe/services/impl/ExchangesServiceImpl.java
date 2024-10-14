package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.ExchangesConverter;
import com.example.caboneftbe.converter.ProcessConverter;
import com.example.caboneftbe.dto.ExchangesDto;
import com.example.caboneftbe.dto.ProcessDto;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.*;
import com.example.caboneftbe.models.Process;
import com.example.caboneftbe.repositories.*;
import com.example.caboneftbe.request.CreateElementaryRequest;
import com.example.caboneftbe.request.CreateProductRequest;
import com.example.caboneftbe.response.CreateProcessResponse;
import com.example.caboneftbe.services.ExchangesService;
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
        Optional<Process> process = processRepository.findById(request.getProcessId());

        if(process.isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Process not exist");
        }
        if(emissionSubstancesRepository.findById(request.getEmissionSubstanceId()).isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Emission substance not exist");
        }

        EmissionSubstances emissionSubstances = emissionSubstancesRepository.findById(request.getEmissionSubstanceId()).get();


        List<MidpointImpactCharacterizationFactors> factors = midpointImpactCharacterizationFactorsRepository.findByEmissionSubstancesId(request.getEmissionSubstanceId());

        String unitTotal = factors.get(0).getUnit().getName();
        Unit unit = new Unit();
        if(unitTotal.startsWith("k")){
            String[]  units = unitTotal.split(" ");
            unit = unitRepository.findByNameUnit(units[0]);
        }
        if(unitTotal.startsWith("m")){
            unitTotal = unitTotal.substring(0, 2);
            unit = unitRepository.findByNameUnit(unitTotal);
        }
        List<Exchanges> exchangesList = exchangesRepository.findAllByProcess(process.get().getId());
        for(Exchanges exchange : exchangesList){
            if(Objects.equals(exchange.getName(), emissionSubstances.getName()) && exchange.getExchangesType().getId() == 2){
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Elementary already exist");
            }
        }

        Exchanges exchanges = new Exchanges();
        exchanges.setExchangesType(exchangesTypeRepository.findById(2L).get());
        exchanges.setName(emissionSubstances.getName());
        exchanges.setProcess(process.get());
        exchanges.setValue(0);
        exchanges.setUnit(unit);
        exchanges.setInput(request.isInput());

        exchanges = exchangesRepository.save(exchanges);

        ProcessDto processDto = processConverter.fromProcessToProcessDto(process.get());

        List<ExchangesDto> list = new ArrayList<>(exchangesConverter.INSTANCE.fromExchangesToExchangesDto(exchangesList));
        processDto.setExchanges(list);

        return CreateProcessResponse.builder()
                .process(processDto)
                .build();
    }

    @Override
    public CreateProcessResponse createProductExchanges(CreateProductRequest request) {
        Optional<Process> process = processRepository.findById(request.getProcessId());
        if(process.isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Process not exist");
        }
        if(!request.isInput()){
            Exchanges exchanges = exchangesRepository.findByProcess(request.getProcessId());
            if(exchanges != null){
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Process already exist product output");
            }
        }

        Unit unit = unitRepository.findByNameUnit("kg");
        List<Exchanges> exchangesList = exchangesRepository.findAllByProcess(process.get().getId());
        for(Exchanges exchange : exchangesList){
            if(Objects.equals(exchange.getName(), request.getName()) && exchange.getExchangesType().getId() == 1){
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Product already exist");
            }
        }

        Exchanges exchanges = new Exchanges();
        exchanges.setExchangesType(exchangesTypeRepository.findById(1L).get());
        exchanges.setName(request.getName());
        exchanges.setProcess(process.get());
        exchanges.setValue(0);
        exchanges.setUnit(unit);
        exchanges.setInput(request.isInput());

        exchanges = exchangesRepository.save(exchanges);

        ProcessDto processDto = processConverter.fromProcessToProcessDto(process.get());

        List<ExchangesDto> list = new ArrayList<>(exchangesConverter.INSTANCE.fromExchangesToExchangesDto(exchangesList));
        processDto.setExchanges(list);

        return CreateProcessResponse.builder()
                .process(processDto)
                .build();
    }
}
