package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UpdateProjectDto;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.request.UpdateProjectDetailRequest;
import com.example.cabonerfbe.response.CreateProjectResponse;
import com.example.cabonerfbe.response.GetAllProjectResponse;
import com.example.cabonerfbe.response.GetProjectByIdResponse;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getProjectListByMethodId(long id);
    Optional<Project> getProjectById(long id);
    CreateProjectResponse createProject(long userId, CreateProjectRequest request);
    GetAllProjectResponse getAllProject(int pageCurrent, int pageSize, String userId);
    GetProjectByIdResponse getById(long id);
    UpdateProjectDto updateDetail(long id, UpdateProjectDetailRequest request);
    List<Project> deleteProject(long id);
}
