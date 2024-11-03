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


    @Mapping(source = "lifeCycleStage",target = "lifeCycleStages")
    ProcessDto fromProcessToProcessDto(Process process);

    @Mapping(source = "lifeCycleStage",target = "lifeCycleStages")
    ProcessDetailDto fromProcessDetailToProcessDto(Process process);

    @Mapping(source = "lifeCycleStage",target = "lifeCycleStages")
    List<ProcessDto> fromListToListDto(List<Process> process);

}
