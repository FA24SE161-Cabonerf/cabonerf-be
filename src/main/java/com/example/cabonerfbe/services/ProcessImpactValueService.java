package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ProcessNodeDto;
import com.example.cabonerfbe.models.ProcessImpactValue;

import java.util.List;
import java.util.UUID;

public interface ProcessImpactValueService {
    ProcessNodeDto computeSystemLevelOfProject(UUID projectId);

    List<ProcessImpactValue> calculateToDesignatedProcess(ProcessNodeDto node);


}
