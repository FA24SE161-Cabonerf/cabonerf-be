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
    private static final int PAGE_INDEX_ADJUSTMENT = 1;
    @Autowired
    private MidpointRepository midpointRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorConverter midpointConverter;
    @Autowired
    private SubstanceRepository esRepository;
    @Autowired
    private EmissionCompartmentRepository ecRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorsRepository factorsRepository;
    @Autowired
    private EmissionSubstanceRepository scRepository;
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

    @Override
    public List<MidpointImpactCharacterizationFactorsResponse> getAllMidpointFactors() {
        List<MidpointImpactCharacterizationFactors> factors = midpointRepository.findAllByStatus(Constants.STATUS_TRUE);
        return midpointConverter.fromMidpointListToMidpointResponseList(factors);

    }

    @Override
    public MidpointImpactCharacterizationFactorsResponse getMidpointFactorById(UUID id) {
        MidpointImpactCharacterizationFactors factor = midpointRepository.findByIdAndStatus(id, Constants.STATUS_TRUE).orElseThrow(()
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

        LifeCycleImpactAssessmentMethod method = methodRepository.findByIdAndStatus(request.getMethodId(), true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_FOUND));

        ImpactCategory category = categoryRepository.findByIdAndStatus(request.getCategoryId(), true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_CATEGORY_FOUND));

        ImpactMethodCategory imc = imcRepository.findByMethodAndCategory(request.getCategoryId(), request.getMethodId())
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_IMPACT_METHOD_CATEGORY_FOUND));

        EmissionSubstance sc;
        if (request.getEmissionSubstanceId() == null) {
            Substance es = esRepository.findByName(request.getName()).orElseGet(() -> {
                Substance newSubstance = new Substance();
                newSubstance.setName(request.getName());
                newSubstance.setChemicalName(Optional.ofNullable(request.getChemicalName()).orElse("-"));
                newSubstance.setMolecularFormula(Optional.ofNullable(request.getMolecularFormula()).orElse("-"));
                newSubstance.setAlternativeFormula(Optional.ofNullable(request.getAlternativeFormula()).orElse("-"));
                newSubstance.setCas(Optional.ofNullable(request.getCas()).orElse("-"));
                return esRepository.save(newSubstance);
            });

            EmissionCompartment ec = ecRepository.getReferenceById(request.getEmissionCompartmentId());
            Unit u = uRepository.findByIdAndStatus(request.getUnitId(), true)
                    .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND));
            boolean isInput = Objects.equals(ec.getName(), "Natural Resource");
            sc = scRepository.checkExistBySubstanceAndCompartment(es.getId(), ec.getId())
                    .orElseGet(() -> scRepository.save(new EmissionSubstance(es, ec, u, isInput)));

            request.setEmissionSubstanceId(sc.getId());
        } else {
            sc = scRepository.findById(request.getEmissionSubstanceId())
                    .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_EMISSION_SUBSTANCE_FOUND));
            if (!sc.getUnit().getId().equals(request.getUnitId())) {
                throw CustomExceptions.badRequest("Units are not similar.");
            }
            if (!sc.getEmissionCompartment().getId().equals(request.getEmissionCompartmentId())) {
                throw CustomExceptions.badRequest("Emission compartments are not similar.");
            }

        }

        Optional<MidpointImpactCharacterizationFactors> f = factorsRepository.checkExistCreate(sc.getId(), request.getMethodId(), request.getCategoryId());
        if (f.isPresent()) {
            throw CustomExceptions.badRequest("Midpoint impact factor already exists.");
        }

        MidpointImpactCharacterizationFactors factors = new MidpointImpactCharacterizationFactors();
        factors.setImpactMethodCategory(imc);
        factors.setDecimalValue(request.getValue());
        factors.setScientificValue(String.format("%.2e", request.getValue()));
        factors.setEmissionSubstance(sc);
        factorsRepository.save(factors);

        return midpointRepository.getWhenCreate(request.getEmissionSubstanceId()).stream()
                .map(midpointConverter::fromQueryResultsToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<MidpointSubstanceFactorsDto> delete(UUID id) {
        EmissionSubstance sc = scRepository.findById(id)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_EMISSION_SUBSTANCE_FOUND));
        sc.setStatus(false);
        scRepository.save(sc);
        List<Object[]> factor = midpointRepository.getWhenCreate(sc.getId());
        List<MidpointSubstanceFactorsDto> response = factor.stream()
                .map(midpointConverter::fromQueryResultsToDto)
                .collect(Collectors.toList());
        return response;
    }
}
