package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.dto.DatasetDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.repositories.OrganizationRepository;
import com.example.cabonerfbe.repositories.ProcessImpactValueRepository;
import com.example.cabonerfbe.repositories.ProcessRepository;
import com.example.cabonerfbe.repositories.ProjectRepository;
import com.example.cabonerfbe.response.DatasetAdminResponse;
import com.example.cabonerfbe.response.DatasetResponse;
import com.example.cabonerfbe.services.DatasetService;
import com.example.cabonerfbe.services.ProcessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The class Dataset service.
 *
 * @author SonPHH.
 */
@Service
public class DatasetServiceImpl implements DatasetService {
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final ProcessRepository processRepository;
    private final ProcessConverter processConverter;
    private final ProcessService processService;
    private final ProcessImpactValueRepository pivRepository;

    /**
     * Instantiates a new Dataset service.
     *
     * @param projectRepository       the project repository
     * @param organizationRepository  the organization repository
     * @param organizationRepository1 the organization repository 1
     * @param processRepository1      the process repository 1
     * @param processConverter        the process converter
     * @param processService          the process service
     * @param pivRepository           the piv repository
     */
    public DatasetServiceImpl(ProjectRepository projectRepository, OrganizationRepository organizationRepository, OrganizationRepository organizationRepository1, ProcessRepository processRepository1, ProcessConverter processConverter, ProcessService processService, ProcessImpactValueRepository pivRepository) {
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository1;
        this.processRepository = processRepository1;
        this.processConverter = processConverter;
        this.processService = processService;
        this.pivRepository = pivRepository;
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

    @Override
    public DatasetAdminResponse get(int pageCurrent, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);

        Page<Process> processPage = keyword == null
                ? processRepository.findDataset(Constants.DATASET_LIST, pageable)
                : processRepository.findDatasetByKeword(Constants.DATASET_LIST, keyword, pageable);

        int totalPage = processPage.getTotalPages();

        List<DatasetDto> dataset = processPage.getContent().stream().map(
                process -> {
                    DatasetDto object = processConverter.fromProcessToDataset(process);
                    object.setImpacts(processService.converterProcess(
                            pivRepository.findByProcessId(process.getId())));
                    return object;
                }
        ).toList();
        if (totalPage < pageCurrent) {
            return DatasetAdminResponse.builder()
                    .pageCurrent(1)
                    .pageSize(0)
                    .totalPage(0)
                    .data(Collections.EMPTY_LIST)
                    .build();
        }

        return DatasetAdminResponse.builder()
                .pageCurrent(pageCurrent)
                .pageSize(pageSize)
                .totalPage(totalPage)
                .data(dataset)
                .build();
    }
}
