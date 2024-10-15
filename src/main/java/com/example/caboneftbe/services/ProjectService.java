package com.example.caboneftbe.services;

import com.example.caboneftbe.models.Project;
import com.example.caboneftbe.request.CreateProjectRequest;
import com.example.caboneftbe.response.CreateProjectResponse;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getProjectListByMethodId(long id);
    Optional<Project> getProjectById(long id);
    CreateProjectResponse createProject(CreateProjectRequest request);
}
