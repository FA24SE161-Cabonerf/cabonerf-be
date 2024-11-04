package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.request.LifeCycleStagesRequest;

import java.util.List;
import java.util.UUID;

public interface LifeCycleStageService {
    List<LifeCycleStageDto> getAll();
    LifeCycleStageDto create(LifeCycleStagesRequest request);
    LifeCycleStageDto update(UUID lifeCycleStagesId, LifeCycleStagesRequest request);
    LifeCycleStageDto delete(UUID lifeCycleStagesId);
}
