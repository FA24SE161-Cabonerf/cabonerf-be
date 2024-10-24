package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.ProcessGetProjectByIdDto;
import com.example.cabonerfbe.models.Process;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProcessConverter {
    ProcessConverter INSTANCE = Mappers.getMapper(ProcessConverter.class);

    ProcessDto fromProcessToProcessDto(Process process);
    ProcessGetProjectByIdDto fromProjectToProcessDto(Process process);

}
