package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCategoryConverter;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.MidpointImpactCategory;
import com.example.cabonerfbe.repositories.MidpointImpactCategoryRepository;
import com.example.cabonerfbe.response.MidpointImpactCategoryResponse;
import com.example.cabonerfbe.services.MidpointImpactCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MidpointImpactCategoryServiceImpl implements MidpointImpactCategoryService {
    @Autowired
    MidpointImpactCategoryRepository midpointImpactCategoryRepository;
    @Autowired
    MidpointImpactCategoryConverter midpointImpactCategoryConverter;

    @Override
    public List<MidpointImpactCategoryResponse> getAllMidpointImpactCategories() {
        List<MidpointImpactCategory> midpointImpactCategoryList = midpointImpactCategoryRepository.findByStatus(Constants.STATUS_TRUE);
        return midpointImpactCategoryConverter.fromListMidpointImpactCategoryToListMidpointImpactCategoryResponse(midpointImpactCategoryList);
    }
}
