package com.example.cabonerfbe.services;

import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.response.CreateProjectResponse;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getProjectListByMethodId(long id);
    Optional<Project> getProjectById(long id);
    CreateProjectResponse createProject(CreateProjectRequest request);
}