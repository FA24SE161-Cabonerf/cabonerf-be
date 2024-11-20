package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ConnectorPercentDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ProcessImpactValueService {
    List<ConnectorPercentDto> computeSystemLevelOfProject(UUID projectId);
    void computeSystemLevelOfProjectBackground(UUID projectId);
}
