package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.EmissionCompartmentDto;
import com.example.caboneftbe.dto.ExchangesDto;
import com.example.caboneftbe.models.EmissionCompartment;
import com.example.caboneftbe.models.Exchanges;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangesConverter {
    ExchangesConverter INSTANCE = Mappers.getMapper(ExchangesConverter.class);

    ExchangesDto fromExchangesToExchangesDto(Exchanges exchanges);
    List<ExchangesDto> fromExchangesToExchangesDto(List<Exchanges> list);
}
