package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import com.example.cabonerfbe.models.ProcessImpactValue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProcessImpactValueConverter {
    ProcessImpactValueConverter INSTANCE = Mappers.getMapper(ProcessImpactValueConverter.class);

    ProcessImpactValueDto fromProcessImpactValueToProcessImpactValueDto(ProcessImpactValue processImpactValue);

    List<ProcessImpactValueDto> fromProcessImpactValueToProcessImpactValueDto(List<ProcessImpactValue> list);
}