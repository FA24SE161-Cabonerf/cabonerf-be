package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.models.Exchanges;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Exchanges converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ExchangesConverter {
    /**
     * The constant INSTANCE.
     */
    ExchangesConverter INSTANCE = Mappers.getMapper(ExchangesConverter.class);

    /**
     * From exchanges to exchanges dto method.
     *
     * @param exchanges the exchanges
     * @return the exchanges dto
     */
    ExchangesDto fromExchangesToExchangesDto(Exchanges exchanges);

    /**
     * From exchanges to exchanges dto method.
     *
     * @param list the list
     * @return the list
     */
    List<ExchangesDto> fromExchangesToExchangesDto(List<Exchanges> list);

    /**
     * From exchange to another exchange method.
     *
     * @param exchange the exchange
     * @return the exchanges
     */
    default Exchanges fromExchangeToAnotherExchange(Exchanges exchange) {
        Exchanges newExchange = new Exchanges();
        newExchange.setName(exchange.getName());
        newExchange.setExchangesType(exchange.getExchangesType());
        newExchange.setValue(exchange.getValue());
        newExchange.setUnit(exchange.getUnit());
        newExchange.setEmissionSubstance(exchange.getEmissionSubstance());
        newExchange.setDescription(exchange.getDescription());
        return newExchange;
    }

}
