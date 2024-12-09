package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.repositories.OrganizationRepository;
import com.example.cabonerfbe.repositories.ProjectRepository;
import com.example.cabonerfbe.response.DatasetResponse;
import com.example.cabonerfbe.services.DatasetService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DatasetServiceImpl implements DatasetService {
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;

    public DatasetServiceImpl(ProjectRepository projectRepository, OrganizationRepository organizationRepository, OrganizationRepository organizationRepository1) {
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository1;
    }

    @Override
    public List<DatasetResponse> getAllDataset(UUID userId, UUID projectId) {
        Project project = projectRepository.findByIdAndStatusTrue(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND)
        );

        if (!project.getUser().getId().equals(userId)) {
            throw CustomExceptions.badRequest(MessageConstants.USER_IS_NOT_OWNER_OF_PROJECT);
        }
        List<UUID> datasetUUIDList = new ArrayList<>();
        datasetUUIDList.add(project.getOrganization().getId());
        datasetUUIDList.addAll(Constants.DATASET_LIST);

        Map<UUID, Organization> datasetMap = organizationRepository.findAllById(datasetUUIDList).stream()
                .collect(Collectors.toMap(Organization::getId, dataset -> dataset));

        return datasetUUIDList.stream()
                .map(uuid -> {
                    Organization dataset = datasetMap.get(uuid);
                    DatasetResponse datasetResponse = new DatasetResponse();
                    datasetResponse.setId(dataset.getId());
                    datasetResponse.setName(dataset.getName());
                    return datasetResponse;
                })
                .collect(Collectors.toList());
    }
}
