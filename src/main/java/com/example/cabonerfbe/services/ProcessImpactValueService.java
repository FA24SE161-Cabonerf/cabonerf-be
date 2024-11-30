package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ProcessNodeDto;

import java.util.UUID;

public interface ProcessImpactValueService {
    ProcessNodeDto computeSystemLevelOfProject(UUID projectId);
}
