package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ExchangesTypeDto;
import com.example.cabonerfbe.models.ExchangesType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Exchanges type converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ExchangesTypeConverter {
    /**
     * The constant INSTANCE.
     */
    ExchangesTypeConverter INSTANCE = Mappers.getMapper(ExchangesTypeConverter.class);

    /**
     * From exchanges type to exchanges type dto method.
     *
     * @param exchangesType the exchanges type
     * @return the exchanges type dto
     */
    ExchangesTypeDto fromExchangesTypeToExchangesTypeDto(ExchangesType exchangesType);
}
