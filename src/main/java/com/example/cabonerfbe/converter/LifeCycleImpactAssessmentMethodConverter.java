package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.dto.MethodDto;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.response.GetNameMethodResponse;
import com.example.cabonerfbe.response.ImpactMethodResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LifeCycleImpactAssessmentMethodConverter {
    LifeCycleImpactAssessmentMethodConverter INSTANCE = Mappers.getMapper(LifeCycleImpactAssessmentMethodConverter.class);

    LifeCycleImpactAssessmentMethodDto fromLifeCycleImpactAssessmentMethodToLifeCycleImpactAssessmentMethodDto(LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod);

    List<LifeCycleImpactAssessmentMethodDto> fromMethodListToMethodDtoList(List<LifeCycleImpactAssessmentMethod> impactMethods);

    MethodDto fromMethodToMethodDto(LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod);

    ImpactMethodResponse fromImpactMethodToImpactMethodResponse(LifeCycleImpactAssessmentMethod impactMethod);

    default GetNameMethodResponse fromModelToName(String name) {
        GetNameMethodResponse response = new GetNameMethodResponse();
        response.setName(name);
        return response;
    }
}
