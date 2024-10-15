package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.ProjectConverter;
import com.example.caboneftbe.converter.ProjectImpactValueConverter;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.ImpactMethodCategory;
import com.example.caboneftbe.models.Project;
import com.example.caboneftbe.models.ProjectImpactValue;
import com.example.caboneftbe.repositories.ImpactMethodCategoryRepository;
import com.example.caboneftbe.repositories.ProjectImpactValueRepository;
import com.example.caboneftbe.repositories.ProjectRepository;
import com.example.caboneftbe.request.CreateProjectRequest;
import com.example.caboneftbe.response.CreateProjectResponse;
import com.example.caboneftbe.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectImpactValueRepository projectImpactValueRepository;
    @Autowired
    private ImpactMethodCategoryRepository impactMethodCategoryRepository;
    @Autowired
    private ProjectConverter projectConverter;
    @Autowired
    private ProjectImpactValueConverter projectImpactValueConverter;

    @Override
    public List<Project> getProjectListByMethodId(long id) {
//        return projectRepository.getProjectLevelDetail(id);
        return null;
    }

    @Override
    public Optional<Project> getProjectById(long id) {
        return Optional.empty();
    }

    @Override
    public CreateProjectResponse createProject(CreateProjectRequest request) {
        if(projectRepository.findByName(request.getName()) != null){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Project name already exists");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setLocation(request.getLocation());
        project.setStatus(true);

        project = projectRepository.save(project);

        List<ImpactMethodCategory> list = impactMethodCategoryRepository.findAll();
        List<ProjectImpactValue> projectImpactValues = new ArrayList<>();
        for (ImpactMethodCategory impactMethodCategory : list) {
            ProjectImpactValue projectImpactValue = new ProjectImpactValue();
            projectImpactValue.setProject(project);
            projectImpactValue.setImpactMethodCategory(impactMethodCategory);
            projectImpactValue.setValue(0);
            projectImpactValue.setStatus(true);
            projectImpactValues.add(projectImpactValue);
        }
        projectImpactValues = projectImpactValueRepository.saveAll(projectImpactValues);

        return CreateProjectResponse.builder()
                .project(projectConverter.INSTANCE.fromProjectToProjectDto(project))
                .projectImpactValue(projectImpactValueConverter.INSTANCE.fromListProjectImpactValueToProjectImpactValueDto(projectImpactValues))
                .build();
    }
}
