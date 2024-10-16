package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ExchangesTypeDto;
import com.example.cabonerfbe.models.ExchangesType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExchangesTypeConverter {
    ExchangesTypeConverter INSTANCE = Mappers.getMapper(ExchangesTypeConverter.class);

    ExchangesTypeDto fromExchangesTypeToExchangesTypeDto(ExchangesType exchangesType);
}
