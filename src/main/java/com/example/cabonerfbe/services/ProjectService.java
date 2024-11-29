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

public interface ProjectService {
    List<Project> getProjectListByMethodId(UUID id);

    ProjectCalculationResponse calculateProject(CalculateProjectRequest request);

    CreateProjectResponse createProject(UUID userId, CreateProjectRequest request);

    GetAllProjectResponse getAllProject(int pageCurrent, int pageSize, UUID userId, UUID methodId, UUID workspaceId);

    GetProjectByIdDto getById(UUID id, UUID userId);

    UpdateProjectDto updateDetail(UUID id, UpdateProjectDetailRequest request, UUID userId);

    List<Project> deleteProject(UUID id, UUID userId);

    GetProjectByIdDto changeProjectMethod(UUID projectId, UUID methodId);

    ResponseEntity<Resource> exportProject(UUID projectId);

    GetProjectByIdDto getProject(Project project);

    List<CarbonIntensityDto> getIntensity(UUID projectId);

    int countAllProject();

    List<GetImpactForAllProjectResponse> countImpactInDashboard();

    UpdateProjectDto updateFavorite(UUID projectId);
}
