package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.SystemBoundaryDto;
import com.example.cabonerfbe.models.SystemBoundary;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SystemBoundaryConverter {
    SystemBoundaryConverter INSTANCE = Mappers.getMapper(SystemBoundaryConverter.class);

    SystemBoundaryDto fromEntityToDto(SystemBoundary systemBoundary);
}