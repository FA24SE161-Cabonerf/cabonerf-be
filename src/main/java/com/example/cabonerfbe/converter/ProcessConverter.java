package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.DatasetDto;
import com.example.cabonerfbe.dto.ObjectLibraryDto;
import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.models.Process;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Process converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ProcessConverter {
    /**
     * The constant INSTANCE.
     */
    ProcessConverter INSTANCE = Mappers.getMapper(ProcessConverter.class);

    /**
     * From process to process dto method.
     *
     * @param process the process
     * @return the process dto
     */
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "overAllProductFlowRequired", target = "overallProductFlowRequired")
    @Mapping(target = "exchanges", ignore = true)
    @Mapping(target = "impacts", ignore = true)
    ProcessDto fromProcessToProcessDto(Process process);

    /**
     * From process detail to process dto method.
     *
     * @param process the process
     * @return the process detail dto
     */
    ProcessDetailDto fromProcessDetailToProcessDto(Process process);

    /**
     * From list to list dto method.
     *
     * @param process the process
     * @return the list
     */
    List<ProcessDto> fromListToListDto(List<Process> process);

    /**
     * From process to object library dto method.
     *
     * @param process the process
     * @return the object library dto
     */
    @Mapping(target = "impacts", ignore = true)
    @Mapping(target = "exchanges", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt")
    ObjectLibraryDto fromProcessToObjectLibraryDto(Process process);

    /**
     * From process to dataset method.
     *
     * @param process the process
     * @return the dataset dto
     */
    DatasetDto fromProcessToDataset(Process process);
}
