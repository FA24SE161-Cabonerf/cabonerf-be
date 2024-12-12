package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.CarbonIntensityDto;
import com.example.cabonerfbe.dto.GetProjectByIdDto;
import com.example.cabonerfbe.dto.UpdateProjectDto;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.request.CalculateProjectRequest;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.request.UpdateProjectDetailRequest;
import com.example.cabonerfbe.response.CreateProjectResponse;
import com.example.cabonerfbe.response.GetAllProjectResponse;
import com.example.cabonerfbe.response.GetImpactForAllProjectResponse;
import com.example.cabonerfbe.response.ProjectCalculationResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * The interface Project service.
 *
 * @author SonPHH.
 */
public interface ProjectService {
    /**
     * Gets project list by method id.
     *
     * @param id the id
     * @return the project list by method id
     */
    List<Project> getProjectListByMethodId(UUID id);

    /**
     * Calculate project method.
     *
     * @param request the request
     * @return the project calculation response
     */
    ProjectCalculationResponse calculateProject(CalculateProjectRequest request);

    /**
     * Create project method.
     *
     * @param userId  the user id
     * @param request the request
     * @return the create project response
     */
    CreateProjectResponse createProject(UUID userId, CreateProjectRequest request);

    /**
     * Gets all project.
     *
     * @param pageCurrent    the page current
     * @param pageSize       the page size
     * @param userId         the user id
     * @param methodId       the method id
     * @param organizationId the organization id
     * @return the all project
     */
    GetAllProjectResponse getAllProject(int pageCurrent, int pageSize, UUID userId, UUID methodId, UUID organizationId);

    /**
     * Gets by id.
     *
     * @param id     the id
     * @param userId the user id
     * @return the by id
     */
    GetProjectByIdDto getById(UUID id, UUID userId);

    /**
     * Update detail method.
     *
     * @param id      the id
     * @param request the request
     * @param userId  the user id
     * @return the update project dto
     */
    UpdateProjectDto updateDetail(UUID id, UpdateProjectDetailRequest request, UUID userId);

    /**
     * Delete project method.
     *
     * @param userId    the user id
     * @param projectId the project id
     * @return the list
     */
    List<Project> deleteProject(UUID userId, UUID projectId);

    /**
     * Change project method method.
     *
     * @param projectId the project id
     * @param methodId  the method id
     * @return the get project by id dto
     */
    GetProjectByIdDto changeProjectMethod(UUID projectId, UUID methodId);

    /**
     * Export project method.
     *
     * @param projectId the project id
     * @return the response entity
     */
    ResponseEntity<Resource> exportProject(UUID projectId);

    /**
     * Gets project.
     *
     * @param project the project
     * @return the project
     */
    GetProjectByIdDto getProject(Project project);

    /**
     * Gets intensity.
     *
     * @param projectId the project id
     * @return the intensity
     */
    List<CarbonIntensityDto> getIntensity(UUID projectId);

    /**
     * Count all project method.
     *
     * @return the int
     */
    int countAllProject();

    /**
     * Count impact in dashboard method.
     *
     * @return the list
     */
    List<GetImpactForAllProjectResponse> countImpactInDashboard();

    /**
     * Update favorite method.
     *
     * @param projectId the project id
     * @return the update project dto
     */
    UpdateProjectDto updateFavorite(UUID projectId);
}
