package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LifeCycleImpactAssessmentMethodConverter {
    LifeCycleImpactAssessmentMethodConverter INSTANCE = Mappers.getMapper(LifeCycleImpactAssessmentMethodConverter.class);

    LifeCycleImpactAssessmentMethodDto fromLifeCycleImpactAssessmentMethodToLifeCycleImpactAssessmentMethodDto(LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod);

    List<LifeCycleImpactAssessmentMethodDto> fromMethodListToMethodDtoList(List<LifeCycleImpactAssessmentMethod> impactMethods);
}
