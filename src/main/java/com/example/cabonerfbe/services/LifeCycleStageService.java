package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.request.LifeCycleStagesRequest;

import java.util.List;
import java.util.UUID;

/**
 * The interface Life cycle stage service.
 *
 * @author SonPHH.
 */
public interface LifeCycleStageService {
    /**
     * Gets all.
     *
     * @return the all
     */
    List<LifeCycleStageDto> getAll();

    /**
     * Create method.
     *
     * @param request the request
     * @return the life cycle stage dto
     */
    LifeCycleStageDto create(LifeCycleStagesRequest request);

    /**
     * Update method.
     *
     * @param lifeCycleStagesId the life cycle stages id
     * @param request           the request
     * @return the life cycle stage dto
     */
    LifeCycleStageDto update(UUID lifeCycleStagesId, LifeCycleStagesRequest request);

    /**
     * Delete method.
     *
     * @param lifeCycleStagesId the life cycle stages id
     * @return the life cycle stage dto
     */
    LifeCycleStageDto delete(UUID lifeCycleStagesId);
}
