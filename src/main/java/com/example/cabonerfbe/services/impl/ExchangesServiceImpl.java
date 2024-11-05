package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.SearchElementaryDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.response.CreateElementaryResponse;
import com.example.cabonerfbe.response.SearchElementaryResponse;
import com.example.cabonerfbe.services.ExchangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExchangesServiceImpl implements ExchangesService {

    @Autowired
    ProcessRepository processRepository;
    @Autowired
    ExchangesRepository exchangesRepository;
    @Autowired
    EmissionCompartmentRepository ecRepository;
    @Autowired
    ExchangesTypeRepository exchangesTypeRepository;
    @Autowired
    MidpointImpactCharacterizationFactorsRepository factorRepository;
    @Autowired
    MidpointImpactCharacterizationFactorConverter factorConverter;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    ProcessConverter processConverter;
    @Autowired
    ExchangesConverter exchangesConverter;
    @Autowired
    SubstancesCompartmentsRepository scRepository;
    @Autowired
    SubstancesCompartmentsConverter scConverter;
    @Autowired
    LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    EmissionSubstancesRepository esRepository;
    @Autowired
    ProcessImpactValueRepository pivRepository;
    @Autowired
    ProcessImpactValueConverter pivConverter;

    @Override
    public ProcessDto createElementaryExchanges(CreateElementaryRequest request) {
        // check process, substanceCompartmentId
        SubstancesCompartments substancesCompartments = scRepository.findById(request.getSubstanceCompartmentId()).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_ELEMENTARY_FLOW_FOUND)
        );
        Process process = processRepository.findByProcessId(request.getProcessId()).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND)
        );
        Optional<Exchanges> e = exchangesRepository.findByElementary(process.getId(),substancesCompartments.getId());
        if(e.isPresent()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR,"Elementary already exist");
        }

        Exchanges newExchange = new Exchanges();
        newExchange.setName(substancesCompartments.getEmissionSubstance().getName());
        newExchange.setExchangesType(exchangesTypeRepository.findByName("Elementary"));
        newExchange.setValue(0);
        newExchange.setUnit(substancesCompartments.getUnit());
        newExchange.setInput(request.isInput());
        newExchange.setProcess(process);
        newExchange.setSubstancesCompartments(substancesCompartments);
        exchangesRepository.save(newExchange);

        ProcessDto dto = processConverter.fromProcessToProcessDto(process);

        dto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(process.getId())));
        dto.setImpacts(pivConverter.fromProcessImpactValueToProcessImpactValueDto(pivRepository.findByProcessId(process.getId())));
        return dto;
    }

    @Override
    public ProcessDto createProductExchanges(CreateProductRequest request) {
        Process p = processRepository.findById(request.getProcessId())
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Process not exist"));

        if(!request.isInput()){
            Optional<Exchanges> exchanges = exchangesRepository.findProductOut(p.getId());
            if(exchanges.isPresent()){
                throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Already output product in process");
            }
        }

        Exchanges e = new Exchanges();
        e.setValue(0);
        e.setName(request.getName());
        e.setInput(request.isInput());
        e.setExchangesType(exchangesTypeRepository.findByName("Product"));
        e.setUnit(unitRepository.findByNameUnit("kg"));
        e.setProcess(p);

        exchangesRepository.save(e);
        ProcessDto dto = processConverter.fromProcessToProcessDto(p);

        dto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(p.getId())));
        dto.setImpacts(pivConverter.fromProcessImpactValueToProcessImpactValueDto(pivRepository.findByProcessId(p.getId())));
        return dto;
    }

    @Override
    @Transactional(readOnly = true) // Add this to optimize read operations
    public SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId,
                                           UUID emissionCompartmentId, UUID impactCategoryId) {
        // Validate method existence first
        LifeCycleImpactAssessmentMethod method = methodRepository.findByIdAndStatus(methodId, true)
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Method not exist"));

        // Get total elements and create pageable
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);
        Page<SubstancesCompartments> scPage;

        // Optimize queries using JOIN FETCH to reduce N+1 problems
        if (keyWord == null && emissionCompartmentId == null) {
            scPage = scRepository.findAllWithJoinFetch(pageable);
        } else if (keyWord != null && emissionCompartmentId == null) {
            scPage = scRepository.searchByKeywordWithJoinFetch(keyWord, pageable);
        } else if (keyWord == null && emissionCompartmentId != null) {
            EmissionCompartment ec = ecRepository.findByIdAndStatus(emissionCompartmentId, true)
                    .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Compartment not exist"));
            scPage = scRepository.searchByCompartmentWithJoinFetch(ec.getId(), pageable);
        } else {
            EmissionCompartment ec = ecRepository.findByIdAndStatus(emissionCompartmentId, true)
                    .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Compartment not exist"));
            scPage = scRepository.searchBySubstanceAndCompartmentWithJoinFetch(keyWord, ec.getId(), pageable);
        }

        int totalPage = scPage.getTotalPages();
        if (pageCurrent > totalPage) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR,
                    Map.of("currentPage", MessageConstants.CURRENT_PAGE_EXCEED_TOTAL_PAGES));
        }

        // Process results in batch using streams
        List<SearchElementaryDto> list = scPage.getContent()
                .parallelStream() // Use parallel stream for better performance with large datasets
                .map(sc -> {
                    SearchElementaryDto dto = new SearchElementaryDto();
                    dto.setSubstancesCompartments(scConverter.ToDto(sc));
                    if (impactCategoryId == null) {
                        List<MidpointImpactCharacterizationFactors> factors =
                                factorRepository.findBySubstanceCompartmentAndMethodWithJoinFetch(sc.getId(), methodId);
                        dto.setFactors(factors.stream()
                                .map(factorConverter::fromMidpointToFactor)
                                .collect(Collectors.toList()));
                    } else {
                        List<MidpointImpactCharacterizationFactors> factors =
                                factorRepository.findBySubstanceCompartmentAndMethodAndCategoryWithJoinFetch(sc.getId(), methodId, impactCategoryId);
                        dto.setFactors(factors.stream()
                                .map(factorConverter::fromMidpointToFactor)
                                .collect(Collectors.toList()));
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        return SearchElementaryResponse.builder()
                .totalPage(totalPage)
                .pageSize(pageSize)
                .pageCurrent(pageCurrent)
                .list(list)
                .build();
    }
}
