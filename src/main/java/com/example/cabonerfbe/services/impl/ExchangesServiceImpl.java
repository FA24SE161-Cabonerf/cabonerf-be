package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.converter.SubstancesCompartmentsConverter;
import com.example.cabonerfbe.dto.SearchElementaryDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.request.SearchElementaryRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;
import com.example.cabonerfbe.response.SearchElementaryResponse;
import com.example.cabonerfbe.services.ExchangesService;
import com.example.specification.EmissionSubstancesSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @Override
    public CreateProcessResponse createElementaryExchanges(CreateElementaryRequest request) {
        return null;
    }

    @Override
    public CreateProcessResponse createProductExchanges(CreateProductRequest request) {
        return null;
    }

    @Override
    public SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId, UUID emissionCompartmentId, UUID impactCategoryId) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);

        Optional<LifeCycleImpactAssessmentMethod> method = methodRepository.findByIdAndStatus(methodId, true);
        if (method.isEmpty()) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Method not exist");
        }

        List<SubstancesCompartments> scList = new ArrayList<>();
        Page<SubstancesCompartments> scPage = null;

        List<EmissionSubstances> esList = new ArrayList<>();

        if (keyWord == null && emissionCompartmentId == null) {
            scPage = scRepository.findAll(pageable);
        } else if (keyWord != null && emissionCompartmentId == null) {
            esList = esRepository.findAll(EmissionSubstancesSpecification.containsKeywordInAllFields(keyWord));
            for (EmissionSubstances es : esList) {
                scList.addAll(scRepository.searchBySubstance(es.getId()));
            }
            scPage = new PageImpl<>(scList, pageable, scList.size());
        } else if(keyWord == null && emissionCompartmentId != null){
            EmissionCompartment ec = ecRepository.findByIdAndStatus(emissionCompartmentId,true).get();
            scList.addAll(scRepository.searchByCompartment(ec.getId()));
            scPage = new PageImpl<>(scList, pageable, scList.size());
        }else{
            EmissionCompartment ec = ecRepository.findByIdAndStatus(emissionCompartmentId,true).get();
            esList = esRepository.findAll(EmissionSubstancesSpecification.containsKeywordInAllFields(keyWord));
            for (EmissionSubstances es : esList) {
                scList.addAll(scRepository.searchBySubstanceAndCompartment(es.getId(),ec.getId()));
            }
            scPage = new PageImpl<>(scList, pageable, scList.size());
        }

        int totalPage = scPage.getTotalPages();

        if (pageCurrent > totalPage) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("currentPage", MessageConstants.CURRENT_PAGE_EXCEED_TOTAL_PAGES));
        }
        List<SearchElementaryDto> list = new ArrayList<>();

        for(SubstancesCompartments sc : scPage){
            List<MidpointImpactCharacterizationFactors> factors = new ArrayList<>();
            if(impactCategoryId == null){
                factors = factorRepository.searchByMethod(sc.getId(),methodId);
            }

            SearchElementaryDto dto = new SearchElementaryDto();
            dto.setSubstancesCompartments(scConverter.ToDto(sc));
            dto.setFactors(factors.stream().map(factorConverter::fromMidpointToFactor).collect(Collectors.toList()));

            list.add(dto);
        }
        return SearchElementaryResponse.builder()
                .totalPage(totalPage)
                .pageSize(pageSize)
                .pageCurrent(pageCurrent)
                .list(list)
                .build();
    }
}
