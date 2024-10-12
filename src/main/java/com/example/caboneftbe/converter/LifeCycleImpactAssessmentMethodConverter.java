package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.ImpactMethodCategoryDto;
import com.example.caboneftbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.caboneftbe.models.ImpactMethodCategory;
import com.example.caboneftbe.models.LifeCycleImpactAssessmentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LifeCycleImpactAssessmentMethodConverter {
    LifeCycleImpactAssessmentMethodConverter INSTANCE = Mappers.getMapper(LifeCycleImpactAssessmentMethodConverter.class);

    LifeCycleImpactAssessmentMethodDto fromLifeCycleImpactAssessmentMethodToLifeCycleImpactAssessmentMethodDto(LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod);
}
