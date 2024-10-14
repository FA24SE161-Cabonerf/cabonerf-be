package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.ConnectorDto;
import com.example.caboneftbe.dto.EmissionCompartmentDto;
import com.example.caboneftbe.models.Connector;
import com.example.caboneftbe.models.EmissionCompartment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConnectorConverter {
    ConnectorConverter INSTANCE = Mappers.getMapper(ConnectorConverter.class);

    ConnectorDto fromConnectorToConnectorDto(Connector connector);
}
