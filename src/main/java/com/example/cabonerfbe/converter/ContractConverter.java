package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ContractDto;
import com.example.cabonerfbe.models.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContractConverter {
    ConnectorConverter INSTANCE = Mappers.getMapper(ConnectorConverter.class);

    ContractDto modelToDto(Contract contract);
}
