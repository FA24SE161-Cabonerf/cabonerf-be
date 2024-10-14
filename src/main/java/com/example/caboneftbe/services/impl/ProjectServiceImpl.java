package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.models.Project;
import com.example.caboneftbe.repositories.ProjectRepository;
import com.example.caboneftbe.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getProjectListByMethodId(long id) {
//        return projectRepository.getProjectLevelDetail(id);
        return null;
    }

    @Override
    public Optional<Project> getProjectById(long id) {
        return Optional.empty();
    }
}
