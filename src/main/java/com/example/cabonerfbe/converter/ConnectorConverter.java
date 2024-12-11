package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ConnectorDto;
import com.example.cabonerfbe.dto.ConnectorPercentDto;
import com.example.cabonerfbe.models.Connector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Connector converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ConnectorConverter {
    /**
     * The constant INSTANCE.
     */
    ConnectorConverter INSTANCE = Mappers.getMapper(ConnectorConverter.class);

    /**
     * From list connector to connector dto method.
     *
     * @param connector the connector
     * @return the list
     */
    @Mapping(source = "startProcess.id", target = "startProcessId")
    @Mapping(source = "endProcess.id", target = "endProcessId")
    @Mapping(source = "startExchanges.id", target = "startExchangesId")
    @Mapping(source = "endExchanges.id", target = "endExchangesId")
    List<ConnectorDto> fromListConnectorToConnectorDto(List<Connector> connector);

    /**
     * From connector to connector dto method.
     *
     * @param connector the connector
     * @return the connector dto
     */
    @Mapping(source = "startProcess.id", target = "startProcessId")
    @Mapping(source = "endProcess.id", target = "endProcessId")
    @Mapping(source = "startExchanges.id", target = "startExchangesId")
    @Mapping(source = "endExchanges.id", target = "endExchangesId")
    ConnectorDto fromConnectorToConnectorDto(Connector connector);

    /**
     * From connector to connector percent dto method.
     *
     * @param connector the connector
     * @return the connector percent dto
     */
    @Mapping(source = "startProcess.id", target = "startProcessId")
    @Mapping(source = "endProcess.id", target = "endProcessId")
    ConnectorPercentDto fromConnectorToConnectorPercentDto(Connector connector);
}
