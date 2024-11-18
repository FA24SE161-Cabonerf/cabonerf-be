package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.GetProjectByIdDto;
import com.example.cabonerfbe.dto.UpdateProjectDto;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.request.UpdateProjectDetailRequest;
import com.example.cabonerfbe.response.CreateProjectResponse;
import com.example.cabonerfbe.response.GetAllProjectResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    List<Project> getProjectListByMethodId(UUID id);
    Optional<Project> getProjectById(UUID id);
    CreateProjectResponse createProject(UUID userId, CreateProjectRequest request);
    GetAllProjectResponse getAllProject(int pageCurrent, int pageSize, UUID userId, UUID methodId);
    GetProjectByIdDto getById(UUID id, UUID userId);
    UpdateProjectDto updateDetail(UUID id, UpdateProjectDetailRequest request);
    List<Project> deleteProject(UUID id);

    GetProjectByIdDto changeProjectMethod(UUID projectId, UUID methodId);
    ResponseEntity<Resource> exportProject(UUID projectId);
}
