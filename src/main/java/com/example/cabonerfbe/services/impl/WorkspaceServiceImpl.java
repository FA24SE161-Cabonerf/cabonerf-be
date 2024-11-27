package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.WorkspaceConverter;
import com.example.cabonerfbe.dto.WorkspaceDto;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.models.UserOrganization;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.models.Workspace;
import com.example.cabonerfbe.repositories.UserOrganizationRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.repositories.WorkspaceRepository;
import com.example.cabonerfbe.services.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkspaceConverter workspaceConverter;
    @Autowired
    private UserOrganizationRepository uoRepository;

    @Override
    public Set<WorkspaceDto> getByUser(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        List<UserOrganization> uo = uoRepository.findByUser(userId);

        List<UUID> organizationIds = uo.stream()
                .map(UserOrganization::getOrganization)
                .map(Organization::getId)
                .collect(Collectors.toList());

        List<Workspace> workspaceOrganization = workspaceRepository.findByOrganization(organizationIds);

        List<Workspace> userWorkspaces = workspaceRepository.findByUser(userId);

        Set<Workspace> workspaces = new HashSet<>(workspaceOrganization);
        workspaces.addAll(userWorkspaces);

        return workspaces.stream()
                .map(workspace -> {
                    WorkspaceDto dto = workspaceConverter.fromWorkspaceToWorkspaceDto(workspace);

                    if (workspace.getOrganization() == null) {
                        dto.setDefault(true);
                    } else {
                        dto.setDefault(!organizationIds.contains(workspace.getOrganization().getId()));
                    }

                    return dto;
                })
                .collect(Collectors.toSet());

    }

}
