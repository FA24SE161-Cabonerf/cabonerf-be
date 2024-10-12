package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.ExchangesDto;
import com.example.caboneftbe.dto.ExchangesTypeDto;
import com.example.caboneftbe.models.Exchanges;
import com.example.caboneftbe.models.ExchangesType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExchangesTypeConverter {
    ExchangesTypeConverter INSTANCE = Mappers.getMapper(ExchangesTypeConverter.class);

    ExchangesTypeDto fromExchangesTypeToExchangesTypeDto(ExchangesType exchangesType);
}
