package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.LifeCycleImpactAssessmentMethodDto;
import com.example.cabonerfbe.dto.MethodDto;
import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import com.example.cabonerfbe.response.GetNameMethodResponse;
import com.example.cabonerfbe.response.ImpactMethodResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Life cycle impact assessment method converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface LifeCycleImpactAssessmentMethodConverter {
    /**
     * The constant INSTANCE.
     */
    LifeCycleImpactAssessmentMethodConverter INSTANCE = Mappers.getMapper(LifeCycleImpactAssessmentMethodConverter.class);

    /**
     * From life cycle impact assessment method to life cycle impact assessment method dto method.
     *
     * @param lifeCycleImpactAssessmentMethod the life cycle impact assessment method
     * @return the life cycle impact assessment method dto
     */
    LifeCycleImpactAssessmentMethodDto fromLifeCycleImpactAssessmentMethodToLifeCycleImpactAssessmentMethodDto(LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod);

    /**
     * From method list to method dto list method.
     *
     * @param impactMethods the impact methods
     * @return the list
     */
    List<LifeCycleImpactAssessmentMethodDto> fromMethodListToMethodDtoList(List<LifeCycleImpactAssessmentMethod> impactMethods);

    /**
     * From method to method dto method.
     *
     * @param lifeCycleImpactAssessmentMethod the life cycle impact assessment method
     * @return the method dto
     */
    MethodDto fromMethodToMethodDto(LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod);

    /**
     * From impact method to impact method response method.
     *
     * @param impactMethod the impact method
     * @return the impact method response
     */
    ImpactMethodResponse fromImpactMethodToImpactMethodResponse(LifeCycleImpactAssessmentMethod impactMethod);

    /**
     * From model to name method.
     *
     * @param name the name
     * @return the get name method response
     */
    default GetNameMethodResponse fromModelToName(String name) {
        GetNameMethodResponse response = new GetNameMethodResponse();
        response.setName(name);
        return response;
    }
}
