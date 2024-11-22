package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.WorkspaceDto;

import java.util.Set;
import java.util.UUID;

public interface WorkspaceService {
    Set<WorkspaceDto> getByUser(UUID userId);
}
