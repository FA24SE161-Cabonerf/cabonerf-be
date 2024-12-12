package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ProcessNodeDto;

import java.util.UUID;

/**
 * The interface Process impact value service.
 *
 * @author SonPHH.
 */
public interface ProcessImpactValueService {
    /**
     * Compute system level of project method.
     *
     * @param projectId the project id
     * @return the process node dto
     */
    ProcessNodeDto computeSystemLevelOfProject(UUID projectId);

    /**
     * Calculate project impact value method.
     *
     * @param projectId the project id
     * @return the process node dto
     */
    ProcessNodeDto calculateProjectImpactValue(UUID projectId);
}
