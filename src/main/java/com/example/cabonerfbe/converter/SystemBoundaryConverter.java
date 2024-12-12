package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.SystemBoundaryDto;
import com.example.cabonerfbe.models.SystemBoundary;
import com.example.cabonerfbe.response.SystemBoundaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface System boundary converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface SystemBoundaryConverter {
    /**
     * The constant INSTANCE.
     */
    SystemBoundaryConverter INSTANCE = Mappers.getMapper(SystemBoundaryConverter.class);

    /**
     * From entity to dto method.
     *
     * @param systemBoundary the system boundary
     * @return the system boundary dto
     */
    SystemBoundaryDto fromEntityToDto(SystemBoundary systemBoundary);

    /**
     * From list entity to list response method.
     *
     * @param systemBoundary the system boundary
     * @return the list
     */
    List<SystemBoundaryResponse> fromListEntityToListResponse(List<SystemBoundary> systemBoundary);
}
