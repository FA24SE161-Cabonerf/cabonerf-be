package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.models.Exchanges;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangesConverter {
    ExchangesConverter INSTANCE = Mappers.getMapper(ExchangesConverter.class);

    ExchangesDto fromExchangesToExchangesDto(Exchanges exchanges);
    List<ExchangesDto> fromExchangesToExchangesDto(List<Exchanges> list);

    default Exchanges fromExchangeToAnotherExchange(Exchanges exchange) {
        Exchanges newExchange = new Exchanges();
        newExchange.setName(exchange.getName());
        newExchange.setExchangesType(exchange.getExchangesType());
        newExchange.setValue(exchange.getValue());
        newExchange.setUnit(exchange.getUnit());
        newExchange.setSubstancesCompartments(exchange.getSubstancesCompartments());
        newExchange.setDescription(exchange.getDescription());
        return newExchange;
    };
}
