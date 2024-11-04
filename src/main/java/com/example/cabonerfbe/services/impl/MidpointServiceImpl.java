package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointSubstanceFactorsDto;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateFactorRequest;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.MidpointImpactCharacterizationFactorsResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import com.example.cabonerfbe.services.MidpointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MidpointServiceImpl implements MidpointService {
    @Autowired
    private MidpointRepository midpointRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorConverter midpointConverter;
    @Autowired
    private EmissionSubstancesRepository esRepository;
    @Autowired
    private EmissionCompartmentRepository ecRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorsRepository factorsRepository;
    @Autowired
    private SubstancesCompartmentsRepository scRepository;
    @Autowired
    private UnitGroupRepository ugRepository;
    @Autowired
    private UnitRepository uRepository;
    @Autowired
    private ImpactCategoryRepository categoryRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private ImpactMethodCategoryRepository imcRepository;

    private static final int PAGE_INDEX_ADJUSTMENT = 1;


    @Override
    public List<MidpointImpactCharacterizationFactorsResponse> getAllMidpointFactors() {
        List<MidpointImpactCharacterizationFactors> factors  = midpointRepository.findAllByStatus(Constants.STATUS_TRUE);
        return midpointConverter.fromMidpointListToMidpointResponseList(factors);

    }

    @Override
    public MidpointImpactCharacterizationFactorsResponse getMidpointFactorById(UUID id) {
        MidpointImpactCharacterizationFactors factor  = midpointRepository.findByIdAndStatus(id, Constants.STATUS_TRUE).orElseThrow(()
                -> CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR)
        );
        return midpointConverter.fromMidpointToMidpointResponse(factor);
    }

    @Override
    public PageList<MidpointSubstanceFactorsResponse> getAllMidpointFactorsAdmin(PaginationRequest request, UUID compartmentId, String keyword) {
        Pageable pageable = PageRequest.of(request.getCurrentPage() - PAGE_INDEX_ADJUSTMENT, request.getPageSize());

        Page<Object[]> midpointSubstanceFactorPage = null;
        if ((keyword == null || keyword.isEmpty()) && compartmentId == null) {
            midpointSubstanceFactorPage = midpointRepository.findAllWithPerspective(pageable);
        } else if (compartmentId == null) {
            midpointSubstanceFactorPage = midpointRepository.findByKeyWord(keyword, pageable);
        } else if (keyword == null || keyword.isEmpty()) {
            midpointSubstanceFactorPage = midpointRepository.filterByCompartment(compartmentId, pageable);
        } else {
            midpointSubstanceFactorPage = midpointRepository.findByKeyWordAndCompartmentId(keyword, compartmentId, pageable);
        }

        int totalPages = midpointSubstanceFactorPage.getTotalPages();

        if (request.getCurrentPage() > totalPages) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("currentPage", MessageConstants.CURRENT_PAGE_EXCEED_TOTAL_PAGES));
        }

        // cái này có thể viết thành 1 converter List<> to List<> thôi. như nhau th nhma viết thế này ngầu vl =))
        // cái nữa là dùng converter line-by-line sẽ ổn định về performance hơn là tạo 1 stream.
        List<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoList = midpointSubstanceFactorPage.getContent().stream()
                .map(midpointConverter::fromQueryResultsToDto)
                .collect(Collectors.toList());

        PageList<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoPageList = new PageList<>();
        midpointSubstanceFactorsDtoPageList.setCurrentPage(request.getCurrentPage());
        midpointSubstanceFactorsDtoPageList.setTotalPage(totalPages);
        midpointSubstanceFactorsDtoPageList.setListResult(midpointSubstanceFactorsDtoList);
        return midpointConverter.fromMidpointSubstanceFactorPageListDtoToMidpointSubstanceFactorPageListResponse(midpointSubstanceFactorsDtoPageList);
    }

    @Override
    public List<MidpointSubstanceFactorsDto> create(CreateFactorRequest request) {

        LifeCycleImpactAssessmentMethod method = methodRepository.findByIdAndStatus(request.getMethodId(),true)
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Method not exist"));

        ImpactCategory category = categoryRepository.findByIdAndStatus(request.getCategoryId(),true)
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR,"Impact category not exist"));


        ImpactMethodCategory imc = imcRepository.findByMethodAndCategory(request.getCategoryId(),request.getMethodId())
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Impact method category not exist"));



        EmissionSubstances es = esRepository.findByName(request.getName())
                .orElseGet(() -> {
                    EmissionSubstances newSubstance = new EmissionSubstances();
                    newSubstance.setName(request.getName());
                    newSubstance.setChemicalName(
                            request.getChemicalName() == null || request.getChemicalName().isEmpty() ? "-" : request.getChemicalName()
                    );
                    newSubstance.setMolecularFormula(
                            request.getMolecularFormula() == null || request.getMolecularFormula().isEmpty() ? "-" : request.getMolecularFormula()
                    );
                    newSubstance.setAlternativeFormula(
                            request.getAlternativeFormula() == null || request.getAlternativeFormula().isEmpty() ? "-" : request.getAlternativeFormula()
                    );

                    return esRepository.save(newSubstance);
                });

        EmissionCompartment ec = ecRepository.getReferenceById(request.getEmissionCompartmentId());

        UnitGroup ug = ugRepository.findByIdAndStatus(request.getUnitGroupId(),true)
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR,"Unit group not exist"));

        Unit u = uRepository.findDefault(ug.getId());

        SubstancesCompartments sc = scRepository.checkExist(es.getId(),ec.getId())
                .orElseGet(() ->{
                    return scRepository.save(new SubstancesCompartments(es,ec,u));
                });

        MidpointImpactCharacterizationFactors factors = new MidpointImpactCharacterizationFactors();
        factors.setImpactMethodCategory(imc);
        factors.setCas(
                request.getCas() == null || request.getCas().isEmpty() ? "-" : request.getCas()
        );
        factors.setDecimalValue(request.getValue());
        factors.setScientificValue(String.format("%.2e", request.getValue()));
        factors.setSubstancesCompartments(sc);
        factorsRepository.save(factors);

        List<Object[]> factor = midpointRepository.getWhenCreate(sc.getId());
        List<MidpointSubstanceFactorsDto> response = factor.stream()
                .map(midpointConverter::fromQueryResultsToDto)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<MidpointSubstanceFactorsDto> delete(UUID id) {
        SubstancesCompartments sc = scRepository.findById(id)
                .orElseThrow(() -> CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Substance Emission not exist"));
        sc.setStatus(false);
        scRepository.save(sc);
        List<Object[]> factor = midpointRepository.getWhenCreate(sc.getId());
        List<MidpointSubstanceFactorsDto> response = factor.stream()
                .map(midpointConverter::fromQueryResultsToDto)
                .collect(Collectors.toList());
        return response;
    }
}
