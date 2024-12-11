package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCategoryConverter;
import com.example.cabonerfbe.dto.MidpointImpactCategoryDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.MidpointImpactCategory;
import com.example.cabonerfbe.models.Unit;
import com.example.cabonerfbe.repositories.MidpointImpactCategoryRepository;
import com.example.cabonerfbe.repositories.UnitRepository;
import com.example.cabonerfbe.request.CreateMidpointImpactCategoryRequest;
import com.example.cabonerfbe.request.UpdateMidpointImpactCategoryRequest;
import com.example.cabonerfbe.response.MidpointImpactCategoryResponse;
import com.example.cabonerfbe.services.MidpointImpactCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The class Midpoint impact category service.
 *
 * @author SonPHH.
 */
@Service
public class MidpointImpactCategoryServiceImpl implements MidpointImpactCategoryService {
    /**
     * The Midpoint impact category repository.
     */
    @Autowired
    MidpointImpactCategoryRepository midpointImpactCategoryRepository;
    /**
     * The Midpoint impact category converter.
     */
    @Autowired
    MidpointImpactCategoryConverter midpointImpactCategoryConverter;
    /**
     * The U repository.
     */
    @Autowired
    UnitRepository uRepository;

    @Override
    public List<MidpointImpactCategoryResponse> getAllMidpointImpactCategories() {
        List<MidpointImpactCategory> midpointImpactCategoryList = midpointImpactCategoryRepository.findByStatus(Constants.STATUS_TRUE);
        return midpointImpactCategoryConverter.fromListMidpointImpactCategoryToListMidpointImpactCategoryResponse(midpointImpactCategoryList);
    }

    @Override
    public List<MidpointImpactCategoryDto> get() {
        List<MidpointImpactCategory> data = midpointImpactCategoryRepository.findByStatus(true);
        return data.stream().map(midpointImpactCategoryConverter::fromMidpointImpactCategoryToMidpointImpactCategoryDto).collect(Collectors.toList());
    }

    @Override
    public MidpointImpactCategoryDto create(CreateMidpointImpactCategoryRequest request) {
        Unit u = uRepository.findByIdAndStatus(request.getUnitId(), true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND));

        MidpointImpactCategory mic = new MidpointImpactCategory(request.getName(), request.getDescription(), request.getAbbr(), u);

        return midpointImpactCategoryConverter.fromMidpointImpactCategoryToMidpointImpactCategoryDto(midpointImpactCategoryRepository.save(mic));
    }

    @Override
    public MidpointImpactCategoryDto update(UUID id, UpdateMidpointImpactCategoryRequest request) {
        MidpointImpactCategory mic = midpointImpactCategoryRepository.findByIdAndStatus(id, true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CATEGORY_FOUND));

        Unit u = uRepository.findByIdAndStatus(request.getUnitId(), true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND));

        mic.setName(request.getName());
        mic.setDescription(request.getDescription());
        mic.setAbbr(request.getAbbr());
        mic.setUnit(u);
        return midpointImpactCategoryConverter.fromMidpointImpactCategoryToMidpointImpactCategoryDto(midpointImpactCategoryRepository.save(mic));
    }

    @Override
    public MidpointImpactCategoryDto delete(UUID id) {
        MidpointImpactCategory mic = midpointImpactCategoryRepository.findByIdAndStatus(id, true)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_MIDPOINT_IMPACT_CATEGORY_FOUND));

        mic.setStatus(false);
        return midpointImpactCategoryConverter.fromMidpointImpactCategoryToMidpointImpactCategoryDto(midpointImpactCategoryRepository.save(mic));
    }
}
