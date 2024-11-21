package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.models.Process;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProcessConverter {
    ProcessConverter INSTANCE = Mappers.getMapper(ProcessConverter.class);

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "overAllProductFlowRequired", target = "overallProductFlowRequired")
    @Mapping(target = "exchanges", ignore = true)
    @Mapping(target = "impacts", ignore = true)
    ProcessDto fromProcessToProcessDto(Process process);

    ProcessDetailDto fromProcessDetailToProcessDto(Process process);

    List<ProcessDto> fromListToListDto(List<Process> process);

}
