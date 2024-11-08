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
    private EmissionSubstanceRepository scRepository;
    @Autowired
    private EmissionSubstanceConverter scConverter;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private ProcessImpactValueRepository pivRepository;
    @Autowired
    private ProcessImpactValueConverter pivConverter;

    public static final String EXCHANGE_TYPE_ELEMENTARY = "Elementary";
    public static final String EXCHANGE_TYPE_PRODUCT = "Product";
    public static final double DEFAULT_VALUE = 0;
    public static final String DEFAULT_PRODUCT_UNIT = "kg";

    @Override
    public ExchangesDto createElementaryExchanges(CreateElementaryRequest request) {
        EmissionSubstance emissionSubstance = findSubstancesCompartments(request.getEmissionSubstanceId(), request.isInput());
        Process process = findProcess(request.getProcessId());

        if (exchangesRepository.findElementary(process.getId(), emissionSubstance.getId()).isPresent()) {
            throw CustomExceptions.badRequest(MessageConstants.ELEMENTARY_EXIST);
        }

        Exchanges newExchange = createNewExchange(emissionSubstance, request.isInput(), process, EXCHANGE_TYPE_ELEMENTARY);

        return exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.save(newExchange));
    }

    @Override
    public ExchangesDto createProductExchanges(CreateProductRequest request) {
        Process process = findProcess(request.getProcessId());

        if (!request.isInput() && exchangesRepository.findProductOut(process.getId()).isPresent()) {
            throw CustomExceptions.badRequest(MessageConstants.PRODUCT_EXIST);
        }

        Exchanges productExchange = createNewExchange(null, request.isInput(), process, EXCHANGE_TYPE_PRODUCT);
        productExchange.setName(request.getName());

        return exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.save(productExchange));
    }

    @Override
    public SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId,
                                           UUID emissionCompartmentId, UUID impactCategoryId, boolean input) {
        validateMethod(methodId);
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);
        Page<EmissionSubstance> scPage = fetchEmissionSubstance(keyWord, emissionCompartmentId, pageable, input);

        List<SearchEmissionSubstanceDto> response = scPage.getContent().parallelStream()
                .map(sc -> buildSearchElementaryDto(sc, impactCategoryId, methodId))
                .collect(Collectors.toList());

        int totalPage = scPage.getTotalPages();
        // Kiểm tra pageCurrent với totalPage và trả về danh sách rỗng nếu cần
        if (pageCurrent > totalPage) {
            return SearchElementaryResponse.builder()
                    .totalPage(0)
                    .pageSize(pageSize)
                    .pageCurrent(1)
                    .list(Collections.emptyList())
                    .build();
        }


        return SearchElementaryResponse.builder()
                .totalPage(totalPage)
                .pageSize(pageSize)
                .pageCurrent(pageCurrent)
                .list(response)
                .build();
    }

    @Override
    public List<EmissionSubstanceDto> getAllAdmin(String keyword) {

        List<EmissionSubstance> list = new ArrayList<>();
        if (keyword == null) {
            list = scRepository.findAll();
        } else {
            list = scRepository.findByKeyword(keyword);
        }

        return list.stream().map(scConverter::modelToDto).collect(Collectors.toList());
    }

    @Override
    public List<ExchangesDto> removeExchange(UUID exchangeId) {
        Exchanges exchange = exchangesRepository.findByIdAndStatus(exchangeId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_EXCHANGE_FOUND)
        );
        exchange.setStatus(Constants.STATUS_FALSE);
        exchangesRepository.save(exchange);
        return exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(exchange.getProcessId()));
    }

    private EmissionSubstance findSubstancesCompartments(UUID id, boolean isInput) {
        return scRepository.findByIdWithInput(id, isInput)
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

    private Exchanges createNewExchange(EmissionSubstance emissionSubstance, boolean isInput,
                                        Process process, String exchangeTypeName) {
        Exchanges exchange = new Exchanges();
        exchange.setExchangesType(exchangesTypeRepository.findByName(exchangeTypeName));
        exchange.setValue(DEFAULT_VALUE);
        exchange.setInput(isInput);
        exchange.setProcess(process);
        exchange.setEmissionSubstance(emissionSubstance);
        if (emissionSubstance != null) {
            exchange.setUnit(emissionSubstance.getUnit());
            exchange.setName(emissionSubstance.getSubstance().getName());
        } else {
            exchange.setUnit(unitRepository.findByNameUnit(DEFAULT_PRODUCT_UNIT));
        }
        return exchange;
    }

    private ProcessDto buildProcessDtoWithExchangesAndImpacts(Process process) {
        ProcessDto dto = processConverter.fromProcessToProcessDto(process);
        dto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(process.getId())));
        dto.setImpacts(pivConverter.fromProcessImpactValueToProcessImpactValueDto(pivRepository.findByProcessId(process.getId())));
        return dto;
    }

    private Page<EmissionSubstance> fetchEmissionSubstance(String keyWord, UUID emissionCompartmentId, Pageable pageable, boolean input) {
        int condition = (keyWord == null ? 0 : 1) + (emissionCompartmentId == null ? 0 : 2);

        return switch (condition) {
            case 0 -> scRepository.findAllWithJoinFetch(input, pageable);
            case 1 -> scRepository.searchByKeywordWithJoinFetch(input, keyWord, pageable);
            case 2 -> {
                EmissionCompartment ec = findEmissionCompartment(emissionCompartmentId);
                yield scRepository.searchByCompartmentWithJoinFetch(input, ec.getId(), pageable);
            }
            case 3 -> {
                EmissionCompartment ec = findEmissionCompartment(emissionCompartmentId);
                yield scRepository.searchBySubstanceAndCompartmentWithJoinFetch(input, keyWord, ec.getId(), pageable);
            }
            default -> throw new IllegalStateException("Unexpected condition: " + condition);
        };
    }

    private EmissionCompartment findEmissionCompartment(UUID emissionCompartmentId) {
        return ecRepository.findByIdAndStatus(emissionCompartmentId, true)
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Compartment not exist"));
    }


    private SearchEmissionSubstanceDto buildSearchElementaryDto(EmissionSubstance sc, UUID impactCategoryId, UUID methodId) {

        SearchEmissionSubstanceDto scDto = scConverter.ToDto(sc);

        List<MidpointImpactCharacterizationFactors> factors = impactCategoryId == null
                ? factorRepository.findBySubstanceCompartmentAndMethodWithJoinFetch(sc.getId(), methodId)
                : factorRepository.findBySubstanceCompartmentAndMethodAndCategoryWithJoinFetch(sc.getId(), methodId, impactCategoryId);

        scDto.setFactors(factors.stream()
                .map(factorConverter::fromMidpointToFactor)
                .collect(Collectors.toList()));

        return scDto;
    }

}
