package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ConnectorDto;
import com.example.cabonerfbe.models.Connector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConnectorConverter {
    ConnectorConverter INSTANCE = Mappers.getMapper(ConnectorConverter.class);

    @Mapping(source = "startProcess.id",target = "startProcessId")
    @Mapping(source = "endProcess.id",target = "endProcessId")
    @Mapping(source = "startExchanges.id",target = "startExchangesId")
    @Mapping(source = "endExchanges.id",target = "endExchangesId")
    List<ConnectorDto> fromListConnectorToConnectorDto(List<Connector> connector);

    @Mapping(source = "startProcess.id",target = "startProcessId")
    @Mapping(source = "endProcess.id",target = "endProcessId")
    @Mapping(source = "startExchanges.id",target = "startExchangesId")
    @Mapping(source = "endExchanges.id",target = "endExchangesId")
    ConnectorDto fromConnectorToConnectorDto(Connector connector);
}
