package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.enums.*;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.*;
import com.example.cabonerfbe.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExchangesServiceImpl implements ExchangesService {

    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private EmissionCompartmentRepository ecRepository;
    @Autowired
    private ExchangesTypeRepository exchangesTypeRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorsRepository factorRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorConverter factorConverter;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private ProcessConverter processConverter;
    @Autowired
    private ExchangesConverter exchangesConverter;
    @Autowired
    private SubstancesCompartmentsRepository scRepository;
    @Autowired
    private SubstancesCompartmentsConverter scConverter;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private ProcessImpactValueRepository pivRepository;
    @Autowired
    private ProcessImpactValueConverter pivConverter;

    @Override
    public ProcessDto createElementaryExchanges(CreateElementaryRequest request) {
        SubstancesCompartments substancesCompartments = findSubstancesCompartments(request.getSubstanceCompartmentId());
        Process process = findProcess(request.getProcessId());

        if (exchangesRepository.findElementary(process.getId(), substancesCompartments.getId()).isPresent()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Elementary already exists");
        }

        Exchanges newExchange = createNewExchange(substancesCompartments, request.isInput(), process, "Elementary", 0);
        exchangesRepository.save(newExchange);

        return buildProcessDtoWithExchangesAndImpacts(process);
    }

    @Override
    public ProcessDto createProductExchanges(CreateProductRequest request) {
        Process process = findProcess(request.getProcessId());

        if (!request.isInput() && exchangesRepository.findProductOut(process.getId()).isPresent()) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Output product already exists in process");
        }

        Exchanges productExchange = createNewExchange(null, request.isInput(), process, "Product", 0);
        productExchange.setName(request.getName());
        productExchange.setUnit(unitRepository.findByNameUnit("kg"));
        exchangesRepository.save(productExchange);

        return buildProcessDtoWithExchangesAndImpacts(process);
    }

    @Override
    public SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId,
                                           UUID emissionCompartmentId, UUID impactCategoryId) {
        validateMethod(methodId);
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);

        Page<SubstancesCompartments> scPage = fetchSubstancesCompartments(keyWord, emissionCompartmentId, pageable);

        int totalPage = scPage.getTotalPages();
        if (pageCurrent > totalPage) {
            return SearchElementaryResponse.builder()
                    .totalPage(0)
                    .pageSize(pageSize)
                    .pageCurrent(1)
                    .list(Collections.emptyList())
                    .build();

        }

        List<SearchSubstancesCompartmentsDto> list = scPage.isEmpty()
                ? searchByCas(impactCategoryId, methodId, keyWord)
                : searchWithoutCas(scPage, impactCategoryId, methodId);

        return SearchElementaryResponse.builder()
                .totalPage(Math.max(totalPage, 1))
                .pageSize(pageSize)
                .pageCurrent(pageCurrent)
                .list(list)
                .build();
    }

    @Override
    public List<SubstancesCompartmentsDto> getAllAdmin(String keyword) {

        List<SubstancesCompartments> list = new ArrayList<>();
        if(keyword == null){
            list = scRepository.findAll();
        }
        else{
            list = scRepository.findByKeyword(keyword);
        }

        return list.stream().map(scConverter::modelToDto).collect(Collectors.toList());
    }

    private SubstancesCompartments findSubstancesCompartments(UUID id) {
        return scRepository.findById(id)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ELEMENTARY_FLOW_FOUND));
    }

    private Process findProcess(UUID id) {
        return processRepository.findByProcessId(id)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND));
    }

    private void validateMethod(UUID methodId) {
        methodRepository.findByIdAndStatus(methodId, true)
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Method not exist"));
    }

    private Exchanges createNewExchange(SubstancesCompartments substancesCompartments, boolean isInput,
                                        Process process, String exchangeTypeName, double value) {
        Exchanges exchange = new Exchanges();
        exchange.setName(substancesCompartments != null ? substancesCompartments.getEmissionSubstance().getName() : null);
        exchange.setExchangesType(exchangesTypeRepository.findByName(exchangeTypeName));
        exchange.setValue(value);
        exchange.setInput(isInput);
        exchange.setProcess(process);
        exchange.setSubstancesCompartments(substancesCompartments);
        return exchange;
    }

    private ProcessDto buildProcessDtoWithExchangesAndImpacts(Process process) {
        ProcessDto dto = processConverter.fromProcessToProcessDto(process);
        dto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(process.getId())));
        dto.setImpacts(pivConverter.fromProcessImpactValueToProcessImpactValueDto(pivRepository.findByProcessId(process.getId())));
        return dto;
    }

    private Page<SubstancesCompartments> fetchSubstancesCompartments(String keyWord, UUID emissionCompartmentId, Pageable pageable) {
        int condition = (keyWord == null ? 0 : 1) + (emissionCompartmentId == null ? 0 : 2);

        return switch (condition) {
            case 0 -> scRepository.findAllWithJoinFetch(pageable);
            case 1 -> scRepository.searchByKeywordWithJoinFetch(keyWord, pageable);
            case 2 -> {
                EmissionCompartment ec = findEmissionCompartment(emissionCompartmentId);
                yield scRepository.searchByCompartmentWithJoinFetch(ec.getId(), pageable);
            }
            case 3 -> {
                EmissionCompartment ec = findEmissionCompartment(emissionCompartmentId);
                yield scRepository.searchBySubstanceAndCompartmentWithJoinFetch(keyWord, ec.getId(), pageable);
            }
            default -> throw new IllegalStateException("Unexpected condition: " + condition);
        };
    }

    private EmissionCompartment findEmissionCompartment(UUID emissionCompartmentId) {
        return ecRepository.findByIdAndStatus(emissionCompartmentId, true)
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Compartment not exist"));
    }

    private List<SearchSubstancesCompartmentsDto> searchWithoutCas(Page<SubstancesCompartments> scPage, UUID impactCategoryId, UUID methodId) {
        return scPage.getContent().parallelStream()
                .map(sc -> buildSearchElementaryDto(sc, impactCategoryId, methodId))
                .collect(Collectors.toList());
    }

    private List<SearchSubstancesCompartmentsDto> searchByCas(UUID impactCategoryId, UUID methodId, String cas) {
        List<MidpointImpactCharacterizationFactors> factors = findFactorsByCas(methodId, cas, impactCategoryId);
        Set<SubstancesCompartments> scList = factors.stream()
                .map(factor -> scRepository.findById(factor.getSubstancesCompartments().getId()).get())
                .collect(Collectors.toSet());

        return scList.parallelStream()
                .map(sc -> buildSearchElementaryDto(sc, factors))
                .collect(Collectors.toList());
    }

    private List<MidpointImpactCharacterizationFactors> findFactorsByCas(UUID methodId, String cas, UUID impactCategoryId) {
        return impactCategoryId == null
                ? factorRepository.findByKeywordWithJoinFetch(methodId, cas)
                : factorRepository.findByCategoryAndKeywordWithJoinFetch(methodId, cas, impactCategoryId);
    }

    private SearchSubstancesCompartmentsDto buildSearchElementaryDto(SubstancesCompartments sc, UUID impactCategoryId, UUID methodId) {

        SearchSubstancesCompartmentsDto scDto = scConverter.ToDto(sc);

        List<MidpointImpactCharacterizationFactors> factors = impactCategoryId == null
                ? factorRepository.findBySubstanceCompartmentAndMethodWithJoinFetch(sc.getId(), methodId)
                : factorRepository.findBySubstanceCompartmentAndMethodAndCategoryWithJoinFetch(sc.getId(), methodId, impactCategoryId);


        scDto.setFactors(factors.stream()
                .map(factorConverter::fromMidpointToFactor)
                .collect(Collectors.toList()));

        return scDto;
    }

    private SearchSubstancesCompartmentsDto buildSearchElementaryDto(SubstancesCompartments sc, List<MidpointImpactCharacterizationFactors> factors) {
        SearchSubstancesCompartmentsDto scDto = scConverter.ToDto(sc);
        scDto.setFactors(factors.stream()
                .map(factorConverter::fromMidpointToFactor)
                .collect(Collectors.toList()));
        return scDto;
    }
}
