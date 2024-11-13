package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.SearchEmissionSubstanceDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.request.UpdateExchangeRequest;
import com.example.cabonerfbe.response.ImpactExchangeResponse;
import com.example.cabonerfbe.response.SearchElementaryResponse;
import com.example.cabonerfbe.services.ExchangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
    @Autowired
    private ProcessServiceImpl processService;
    @Autowired
    private UnitServiceImpl unitService;
    @Autowired
    private ProcessImpactValueServiceImpl processImpactValueService;

    public static final String EXCHANGE_TYPE_ELEMENTARY = "Elementary";
    public static final String EXCHANGE_TYPE_PRODUCT = "Product";
    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);
    public static final String DEFAULT_PRODUCT_UNIT = "kg";
    @Autowired
    private ConnectorRepository connectorRepository;

    @Override
    public List<ExchangesDto> createElementaryExchanges(CreateElementaryRequest request) {
        UUID processId = request.getProcessId();
        EmissionSubstance emissionSubstance = findSubstancesCompartments(request.getEmissionSubstanceId(), request.isInput());
        Process process = findProcess(processId);

        if (exchangesRepository.findElementary(process.getId(), emissionSubstance.getId()).isPresent()) {
            throw CustomExceptions.badRequest(MessageConstants.ELEMENTARY_EXIST);
        }

        Exchanges newExchange = createNewExchange(emissionSubstance, request.isInput(), process, EXCHANGE_TYPE_ELEMENTARY);
        exchangesRepository.save(newExchange);
        return exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(processId));
    }

    @Override
    public List<ExchangesDto> createProductExchanges(CreateProductRequest request) {
        UUID processId = request.getProcessId();

        Process process = findProcess(processId);

        if (!request.isInput() && exchangesRepository.findProductOut(process.getId()).isPresent()) {
            throw CustomExceptions.badRequest(MessageConstants.PRODUCT_EXIST);
        }

        Exchanges productExchange = createNewExchange(null, request.isInput(), process, EXCHANGE_TYPE_PRODUCT);
        productExchange.setName(request.getName());
        exchangesRepository.save(productExchange);

        return exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(processId));
    }

    @Override
    public SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId,
                                           UUID emissionCompartmentId, UUID impactCategoryId, boolean input) {
        validateMethod(methodId);
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);
        Page<EmissionSubstance> scPage = fetchEmissionSubstance(keyWord, emissionCompartmentId, pageable, input, impactCategoryId);

        List<SearchEmissionSubstanceDto> response = scPage.getContent().parallelStream()
                .map(sc -> buildSearchElementaryDto(sc, impactCategoryId, methodId))
                .collect(Collectors.toList());

        int totalPage = scPage.getTotalPages();
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
    public ImpactExchangeResponse removeElementaryExchange(UUID exchangeId) {
        Exchanges exchange = exchangesRepository.findByIdAndStatus(exchangeId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_EXCHANGE_FOUND)
        );

        BigDecimal initialValue = exchange.getValue();

        exchange.setValue(DEFAULT_VALUE);
        exchange.setStatus(Constants.STATUS_FALSE);

        exchangesRepository.save(exchange);

        if (exchange.getExchangesType().getName().equals(EXCHANGE_TYPE_ELEMENTARY)) {
            processImpactValueService.computeProcessImpactValueSingleExchange(exchange.getProcess(), exchange, initialValue);
        }

        return impactExchangeResponseBuilder(exchange.getProcessId());
    }

    @Override
    public List<ExchangesDto> removeProductExchange(UUID exchangeId) {
        Exchanges exchange = exchangesRepository.findByIdAndStatus(exchangeId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_EXCHANGE_FOUND)
        );

        BigDecimal initialValue = exchange.getValue();

        exchange.setValue(DEFAULT_VALUE);
        exchange.setStatus(Constants.STATUS_FALSE);

        exchangesRepository.save(exchange);
        return exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(exchange.getProcessId()));
    }

    @Override
    public ImpactExchangeResponse updateElementaryExchange(UUID exchangeId, UpdateExchangeRequest request) {
        UUID unitId = request.getUnitId();
        UUID processId = request.getProcessId();
        BigDecimal value = request.getValue();

        Exchanges exchange = exchangesRepository.findByIdAndStatus(exchangeId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_EXCHANGE_FOUND)
        );

        if (!exchange.getProcessId().equals(processId)) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_AND_PROCESS_DIFFERENT);
        }

        Process process = processRepository.findByProcessId(processId).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND)
        );

        BigDecimal initialValue = exchange.getValue();

        if (unitId != null && !unitId.equals(exchange.getUnit().getId())) {
            Unit unit = unitRepository.findByIdAndStatus(unitId, Constants.STATUS_TRUE).orElseThrow(
                    () -> CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND)
            );

            initialValue = unitService.convertValue(exchange.getUnit(), initialValue, unit);
            exchange.setUnit(unit);
        }

        if (value != null) {
            exchange.setValue(value);
        }

        exchangesRepository.save(exchange);

        processImpactValueService.computeProcessImpactValueSingleExchange(process, exchange, initialValue);

        return impactExchangeResponseBuilder(processId);
    }

    @Override
    public List<ExchangesDto> updateProductExchange(UUID exchangeId, UpdateExchangeRequest request) {
        String name = request.getName();
        UUID unitId = request.getUnitId();
        UUID processId = request.getProcessId();
        BigDecimal value = request.getValue();

        Exchanges exchange = exchangesRepository.findByIdAndStatus(exchangeId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_EXCHANGE_FOUND)
        );

        if (!exchange.getProcessId().equals(processId)) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_AND_PROCESS_DIFFERENT);
        }

        List<UUID> connectedExchangeIdList = new ArrayList<>();
        connectedExchangeIdList.add(exchangeId);
        if (unitId != null || name != null) {
            List<Connector> connectorList = connectorRepository.findConnectorToExchange(exchangeId);
            for (Connector connector : connectorList) {
                connectedExchangeIdList.add(connector.getStartExchanges().getId().equals(exchangeId)
                        ? connector.getEndExchanges().getId()
                        : connector.getStartExchanges().getId());
            }
            if (unitId != null && !unitId.equals(exchange.getUnit().getId())) {
                Unit unit = unitRepository.findByIdAndStatus(unitId, Constants.STATUS_TRUE).orElseThrow(
                        () -> CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND)
                );
                UnitGroup initialUnitGroup = exchange.getUnit().getUnitGroup();
                if (!unit.getUnitGroup().equals(initialUnitGroup)) {
                    for (UUID connectorId : connectedExchangeIdList) {
                        Exchanges connectedExchange = exchangesRepository.findByIdAndStatus(connectorId, Constants.STATUS_TRUE).orElseThrow(
                                () -> CustomExceptions.badRequest(MessageConstants.NO_EXCHANGE_FOUND)
                        );
                        connectedExchange.setUnit(unit);
                        exchangesRepository.save(connectedExchange);
                    }
                }
            }
            if (name != null && !name.equals(exchange.getName())) {
                for (UUID connectorId : connectedExchangeIdList) {
                    Exchanges connectedExchange = exchangesRepository.findByIdAndStatus(connectorId, Constants.STATUS_TRUE).orElseThrow(
                            () -> CustomExceptions.badRequest(MessageConstants.NO_EXCHANGE_FOUND)
                    );
                    connectedExchange.setName(name);
                    exchangesRepository.save(connectedExchange);
                }
            }
        }

        if (value != null) {
            exchange.setValue(value);
        }

        exchangesRepository.save(exchange);

        return exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByIdMatches(connectedExchangeIdList));
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

    private Page<EmissionSubstance> fetchEmissionSubstance(
            String keyWord, UUID emissionCompartmentId, Pageable pageable, boolean input, UUID categoryId) {
        int condition = (keyWord == null ? 0 : 1) +
                (emissionCompartmentId == null ? 0 : 2) +
                (categoryId == null ? 0 : 4);

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
            case 4 -> scRepository.findAllWithJoinFetchCategory(input, categoryId, pageable);
            case 5 -> scRepository.searchByKeywordWithJoinFetchCategory(input, keyWord, categoryId, pageable);
            case 6 -> {
                EmissionCompartment ec = findEmissionCompartment(emissionCompartmentId);
                yield scRepository.searchByCompartmentWithJoinFetchCategory(input, ec.getId(), categoryId, pageable);
            }
            case 7 -> {
                EmissionCompartment ec = findEmissionCompartment(emissionCompartmentId);
                yield scRepository.searchBySubstanceAndCompartmentWithJoinFetchCategory(input, keyWord, ec.getId(), categoryId, pageable);
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

    private ImpactExchangeResponse impactExchangeResponseBuilder(UUID processId) {
        return ImpactExchangeResponse.builder()
                .exchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(processId)))
                .impacts(processService.converterProcess(pivRepository.findByProcessId(processId)))
                .build();
    }

}
