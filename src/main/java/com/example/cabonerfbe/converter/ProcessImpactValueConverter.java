package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import com.example.cabonerfbe.models.ProcessImpactValue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Process impact value converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ProcessImpactValueConverter {
    /**
     * The constant INSTANCE.
     */
    ProcessImpactValueConverter INSTANCE = Mappers.getMapper(ProcessImpactValueConverter.class);

    /**
     * From process impact value to process impact value dto method.
     *
     * @param processImpactValue the process impact value
     * @return the process impact value dto
     */
    ProcessImpactValueDto fromProcessImpactValueToProcessImpactValueDto(ProcessImpactValue processImpactValue);

    /**
     * From process impact value to process impact value dto method.
     *
     * @param list the list
     * @return the list
     */
    List<ProcessImpactValueDto> fromProcessImpactValueToProcessImpactValueDto(List<ProcessImpactValue> list);


}
