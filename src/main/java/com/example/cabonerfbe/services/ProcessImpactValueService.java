package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ConnectorPercentDto;
import com.example.cabonerfbe.dto.GetProjectByIdDto;

import com.example.cabonerfbe.response.ProjectCalculationResponse;

import java.util.UUID;

public interface ProcessImpactValueService {
    void computeSystemLevelOfProject(UUID projectId);
    void computeSystemLevelOfProjectBackground(UUID projectId);
//    ProjectCalculationResponse computeSystemLevelOfProject(UUID projectId);
}
