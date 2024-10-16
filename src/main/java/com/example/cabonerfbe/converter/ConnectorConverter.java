package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ConnectorDto;
import com.example.cabonerfbe.models.Connector;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConnectorConverter {
    ConnectorConverter INSTANCE = Mappers.getMapper(ConnectorConverter.class);

    ConnectorDto fromConnectorToConnectorDto(Connector connector);
}
