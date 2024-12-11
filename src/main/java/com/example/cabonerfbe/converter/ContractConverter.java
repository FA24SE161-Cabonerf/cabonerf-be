package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ContractDto;
import com.example.cabonerfbe.models.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Contract converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ContractConverter {
    /**
     * The constant INSTANCE.
     */
    ConnectorConverter INSTANCE = Mappers.getMapper(ConnectorConverter.class);

    /**
     * Model to dto method.
     *
     * @param contract the contract
     * @return the contract dto
     */
    ContractDto modelToDto(Contract contract);
}
