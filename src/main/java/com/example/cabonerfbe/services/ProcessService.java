package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import com.example.cabonerfbe.dto.ProcessNodeDto;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.DeleteProcessResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Process service.
 *
 * @author SonPHH.
 */
public interface ProcessService {
    /**
     * Create process method.
     *
     * @param request the request
     * @return the process dto
     */
    ProcessDto createProcess(CreateProcessRequest request);

    /**
     * Gets process by id.
     *
     * @param id the id
     * @return the process by id
     */
    ProcessDto getProcessById(UUID id);

    /**
     * Gets all processes by project id.
     *
     * @param projectId the project id
     * @return the all processes by project id
     */
    List<ProcessDto> getAllProcessesByProjectId(UUID projectId);

    /**
     * Update process method.
     *
     * @param id      the id
     * @param request the request
     * @return the process detail dto
     */
    ProcessDetailDto updateProcess(UUID id, UpdateProcessRequest request);

    /**
     * Delete process method.
     *
     * @param id the id
     * @return the delete process response
     */
    DeleteProcessResponse deleteProcess(UUID id);

    /**
     * Construct list process node dto method.
     *
     * @param projectId the project id
     * @return the process node dto
     */
    ProcessNodeDto constructListProcessNodeDto(UUID projectId);

    /**
     * Converter process method.
     *
     * @param list the list
     * @return the list
     */
    List<ProcessImpactValueDto> converterProcess(List<ProcessImpactValue> list);

    /**
     * Calculation fast method.
     *
     * @param projectId the project id
     * @return the process node dto
     */
    ProcessNodeDto calculationFast(UUID projectId);

    /**
     * Convert process to object library method.
     *
     * @param process                the process
     * @param projectImpactValueList the project impact value list
     */
    void convertProcessToObjectLibrary(Process process, List<ProjectImpactValue> projectImpactValueList);

    /**
     * Convert object library to process dto method.
     *
     * @param process the process
     * @param project the project
     * @return the process dto
     */
    ProcessDto convertObjectLibraryToProcessDto(Process process, Project project);

    /**
     * Create new process impact value method.
     *
     * @param process        the process
     * @param methodCategory the method category
     * @return the process impact value
     */
    ProcessImpactValue createNewProcessImpactValue(Process process, ImpactMethodCategory methodCategory);

}
