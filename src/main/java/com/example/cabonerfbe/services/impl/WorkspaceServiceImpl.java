package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.WorkspaceConverter;
import com.example.cabonerfbe.dto.WorkspaceDto;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.models.Process;
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
                .orElseThrow(() -> CustomExceptions.notFound("User not exist"));

        List<UserOrganization> uo = uoRepository.findByUser(userId);

        List<UUID> organizationIds = uo.stream()
                .map(UserOrganization::getOrganization)
                .map(Organization::getId)
                .collect(Collectors.toList());

        List<Workspace> workspaceOrganization = workspaceRepository.findByOrganization(organizationIds);

        Set<Workspace> workspaces = new HashSet<>(!workspaceOrganization.isEmpty() ? workspaceOrganization : Collections.emptySet());

        List<Workspace> w = workspaceRepository.findByUser(userId);

        workspaces.addAll( !w.isEmpty() ? w : Collections.emptyList());

        return !workspaces.isEmpty()
                ? workspaces.stream().map(workspaceConverter::fromWorkspaceToWorkspaceDto).collect(Collectors.toSet())
                : Collections.emptySet();
    }
}
