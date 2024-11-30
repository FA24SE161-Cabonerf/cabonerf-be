package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.dto.ObjectLibraryDto;
import com.example.cabonerfbe.dto.SearchObjectLibraryDto;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.PagingKeywordMethodRequest;
import com.example.cabonerfbe.services.ObjectLibraryService;
import com.example.cabonerfbe.services.ProcessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ObjectLibraryServiceImpl implements ObjectLibraryService {

    private final OrganizationRepository organizationRepository;
    private final ProcessRepository processRepository;
    private final LifeCycleImpactAssessmentMethodRepository methodRepository;
    private final ProcessConverter processConverter;
    private final ProcessService processService;
    private final ProcessImpactValueRepository processImpactValueRepository;
    private final ExchangesConverter exchangesConverter;
    private final ExchangesRepository exchangesRepository;

    public ObjectLibraryServiceImpl(ProcessService processService, OrganizationRepository organizationRepository, ProcessRepository processRepository, LifeCycleImpactAssessmentMethodRepository methodRepository, ProcessConverter processConverter, ProcessImpactValueRepository processImpactValueRepository, ExchangesConverter exchangesConverter, ExchangesRepository exchangesRepository, ExchangesConverter exchangesConverter1, ExchangesRepository exchangesRepository1) {
        this.organizationRepository = organizationRepository;
        this.processRepository = processRepository;
        this.methodRepository = methodRepository;
        this.processConverter = processConverter;
        this.processService = processService;
        this.processImpactValueRepository = processImpactValueRepository;
        this.exchangesConverter = exchangesConverter1;
        this.exchangesRepository = exchangesRepository1;
    }

    @Transactional
    @Override
    public SearchObjectLibraryDto searchObjectLibraryOfOrganization(UUID organizationId, PagingKeywordMethodRequest request) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_ORGANIZATION_FOUND)
        );

        validateMethod(request.getMethodId());

        Pageable pageable = PageRequest.of(request.getCurrentPage() - 1, request.getPageSize());

        Page<Process> processPage = processRepository.findObjectLibrary(organizationId, request.getMethodId(), request.getKeyword(), pageable);

        List<ObjectLibraryDto> objectLibraryList = processPage.getContent().parallelStream().map(
                process -> {
                    ObjectLibraryDto object = processConverter.fromProcessToObjectLibraryDto(process);
                    object.setImpacts(processService.converterProcess(
                            processImpactValueRepository.findByProcessId(process.getId())));
                    object.setExchanges(exchangesConverter.fromExchangesToExchangesDto(
                            exchangesRepository.findAllByProcess(process.getId())));
                    return object;
                }
        ).toList();

        int totalPage = processPage.getTotalPages();

        boolean exceeded = request.getCurrentPage() > totalPage;

        return SearchObjectLibraryDto.builder()
                .totalPage(exceeded ? 0 : totalPage)
                .pageSize(request.getPageSize())
                .pageCurrent(exceeded ? 1 : request.getCurrentPage())
                .objectLibraryList(exceeded ? Collections.emptyList() : objectLibraryList)
                .build();

    }

    private void validateMethod(UUID methodId) {
        methodRepository.findByIdAndStatus(methodId, true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND, Collections.EMPTY_LIST));
    }
}
