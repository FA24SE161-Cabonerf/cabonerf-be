package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.ProjectCalculationResponse;

import java.util.UUID;

public interface ProcessImpactValueService {
    ProjectCalculationResponse computeSystemLevelOfProject(UUID projectId);
}
