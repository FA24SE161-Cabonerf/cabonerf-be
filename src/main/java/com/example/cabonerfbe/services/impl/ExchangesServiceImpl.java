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
import com.example.cabonerfbe.response.UpdateProductExchangeResponse;
import com.example.cabonerfbe.services.ExchangesService;
import com.example.cabonerfbe.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExchangesServiceImpl implements ExchangesService {

    public static final String EXCHANGE_TYPE_ELEMENTARY = "Elementary";
    public static final String EXCHANGE_TYPE_PRODUCT = "Product";
    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);
    public static final String DEFAULT_PRODUCT_UNIT = "kg";
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
    private ProcessService processService;
    @Autowired
    private UnitServiceImpl unitService;
    @Autowired
    private ProcessImpactValueServiceImpl processImpactValueService;
    @Autowired
    private ConnectorServiceImpl connectorService;
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

        validateObjectLibrary(exchange.getProcess());

        BigDecimal initialValue = exchange.getValue();

        exchange.setValue(BigDecimal.ZERO);
        exchange.setStatus(Constants.STATUS_FALSE);

        exchangesRepository.save(exchange);

        if (exchange.getExchangesType().getName().equals(EXCHANGE_TYPE_ELEMENTARY)) {
            processImpactValueService.computeProcessImpactValueSingleExchange(exchange.getProcess(), exchange, initialValue);
        }

        return impactExchangeResponseBuilder(exchange);
    }

    @Transactional
    @Override
    public List<ExchangesDto> removeProductExchange(UUID exchangeId) {
        Exchanges exchange = exchangesRepository.findByIdAndStatus(exchangeId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_EXCHANGE_FOUND)
        );

        validateObjectLibrary(exchange.getProcess());

        exchange.setValue(DEFAULT_VALUE);
        exchange.setStatus(Constants.STATUS_FALSE);

        exchangesRepository.save(exchange);


        Thread deleteConnectorThread = new Thread(() -> connectorService.deleteAssociatedConnectors(exchangeId, Constants.DELETE_CONNECTOR_TYPE_EXCHANGE));
        deleteConnectorThread.start();

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

        validateObjectLibrary(exchange.getProcess());

        BigDecimal initialValue = exchange.getValue();
        System.out.println("initial value before converted to " + exchange.getUnit().getName() + " unit: " + initialValue);

        if (unitId != null && !unitId.equals(exchange.getUnit().getId())) {
            Unit unit = unitRepository.findByIdAndStatus(unitId, Constants.STATUS_TRUE).orElseThrow(
                    () -> CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND)
            );

            initialValue = unitService.convertValue(exchange.getUnit(), initialValue, unit);
            System.out.println("initial value after converted to " + unit.getName() + " unit: " + initialValue);
            if (value != null) {
                initialValue = initialValue.setScale(value.scale(), RoundingMode.HALF_UP);
            }
            exchange.setUnit(unit);
        }

        if (value != null) {
            System.out.println("old exchange value: " + exchange.getValue() + " | new exchange value: " + value);
            exchange.setValue(value);
        }

        exchangesRepository.save(exchange);

        processImpactValueService.computeProcessImpactValueSingleExchange(exchange.getProcess(), exchange, initialValue);

        return impactExchangeResponseBuilder(exchange);
    }

    @Override
    public List<UpdateProductExchangeResponse> updateProductExchange(UUID exchangeId, UpdateExchangeRequest request) {
        String name = request.getName();
        UUID unitId = request.getUnitId();
        UUID processId = request.getProcessId();
        BigDecimal value = request.getValue();

        if (name.isBlank()) {
            throw CustomExceptions.validator(MessageConstants.PRODUCT_NAME_CANNOT_BE_BLANK, Map.of("name", MessageConstants.PRODUCT_NAME_CANNOT_BE_BLANK));
        }

        Exchanges exchange = exchangesRepository.findByIdAndStatus(exchangeId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_EXCHANGE_FOUND)
        );

        if (!exchange.getProcessId().equals(processId)) {
            throw CustomExceptions.badRequest(MessageConstants.EXCHANGE_AND_PROCESS_DIFFERENT);
        }

        validateObjectLibrary(exchange.getProcess());

        List<UUID> connectedExchangeIdList = new ArrayList<>();
        connectedExchangeIdList.add(exchangeId);
        boolean isLibraryConnected = false;
        List<Connector> connectorList = connectorRepository.findConnectorToExchange(exchangeId);
        if (connectorList.size() == 1) {
            Connector connector = connectorList.get(0);
            // validate if connected to an object library process => cannot change its unit group
            if (connector.getEndProcess().isLibrary() || connector.getStartProcess().isLibrary()) {
                isLibraryConnected = true;
            }
        }

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
                if (isLibraryConnected) {
                    throw CustomExceptions.validator(MessageConstants.CANNOT_UPDATE_UNIT_GROUP_OF_LIBRARY_CONNECTED_PROCESS,
                            Map.of("unit", MessageConstants.CANNOT_UPDATE_UNIT_GROUP_OF_LIBRARY_CONNECTED_PROCESS));
                }
                for (UUID connectorId : connectedExchangeIdList) {
                    Exchanges connectedExchange = exchangesRepository.findByIdAndStatus(connectorId, Constants.STATUS_TRUE).orElseThrow(
                            () -> CustomExceptions.badRequest(MessageConstants.NO_EXCHANGE_FOUND)
                    );
                    connectedExchange.setUnit(unit);
                    exchangesRepository.save(connectedExchange);
                }
            } else {
                exchange.setUnit(unit);
            }
        }
        if (!name.equals(exchange.getName())) {
            if (isLibraryConnected) {
                throw CustomExceptions.validator(MessageConstants.CANNOT_UPDATE_NAME_OF_LIBRARY_CONNECTED_PROCESS,
                        Map.of("name", MessageConstants.CANNOT_UPDATE_NAME_OF_LIBRARY_CONNECTED_PROCESS));
            }
            for (UUID connectorId : connectedExchangeIdList) {
                Exchanges connectedExchange = exchangesRepository.findByIdAndStatus(connectorId, Constants.STATUS_TRUE).orElseThrow(
                        () -> CustomExceptions.badRequest(MessageConstants.NO_EXCHANGE_FOUND)
                );
                connectedExchange.setName(name);
                exchangesRepository.save(connectedExchange);
            }
        }

        if (value != null) {
            exchange.setValue(value);
        }
        exchangesRepository.save(exchange);


        return exchangesRepository.findAllByIdMatches(connectedExchangeIdList)
                .stream()
                .map(e -> new UpdateProductExchangeResponse(e.getProcessId(), exchangesConverter.fromExchangesToExchangesDto(e)))
                .collect(Collectors.toList());
    }

    private EmissionSubstance findSubstancesCompartments(UUID id, boolean isInput) {
        return scRepository.findByIdWithInput(id, isInput)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ELEMENTARY_FLOW_FOUND));
    }

    private Process findProcess(UUID id) {
        Process process = processRepository.findByProcessId(id)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_PROCESS_FOUND));
        validateObjectLibrary(process);
        return process;
    }

    private void validateMethod(UUID methodId) {
        methodRepository.findByIdAndStatus(methodId, true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND, Collections.EMPTY_LIST));
    }

    private void validateObjectLibrary(Process process) {
        if (process.isLibrary()) {
            throw CustomExceptions.badRequest(MessageConstants.CANNOT_EDIT_OBJECT_LIBRARY_PROCESS);
        }
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
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_EMISSION_COMPARTMENT_FOUND, Collections.EMPTY_LIST));
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

    private ImpactExchangeResponse impactExchangeResponseBuilder(Exchanges exchange) {
        return ImpactExchangeResponse.builder()
                .exchange(exchangesConverter.fromExchangesToExchangesDto(exchange))
                .impacts(processService.converterProcess(pivRepository.findByProcessId(exchange.getProcessId())))
                .build();
    }


}
