package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.models.Exchanges;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangesConverter {
    ExchangesConverter INSTANCE = Mappers.getMapper(ExchangesConverter.class);

    ExchangesDto fromExchangesToExchangesDto(Exchanges exchanges);
    List<ExchangesDto> fromExchangesToExchangesDto(List<Exchanges> list);
}
