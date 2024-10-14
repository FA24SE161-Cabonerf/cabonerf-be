package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.ExchangesDto;
import com.example.caboneftbe.dto.ProcessImpactValueDto;
import com.example.caboneftbe.models.Exchanges;
import com.example.caboneftbe.models.ProcessImpactValue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProcessImpactValueConverter {
    ProcessImpactValueConverter INSTANCE = Mappers.getMapper(ProcessImpactValueConverter.class);

    ProcessImpactValueDto fromProcessImpactValueToProcessImpactValueDto(ProcessImpactValue processImpactValue);

    List<ProcessImpactValueDto> fromProcessImpactValueToProcessImpactValueDto(List<ProcessImpactValue> list);
}
