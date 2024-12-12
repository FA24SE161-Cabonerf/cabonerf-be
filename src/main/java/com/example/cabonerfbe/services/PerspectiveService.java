package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreatePerspectiveRequest;
import com.example.cabonerfbe.request.UpdatePerspectiveRequest;
import com.example.cabonerfbe.response.PerspectiveResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Perspective service.
 *
 * @author SonPHH.
 */
public interface PerspectiveService {
    /**
     * Gets all perspective.
     *
     * @return the all perspective
     */
    List<PerspectiveResponse> getAllPerspective();

    /**
     * Gets perspective by id.
     *
     * @param id the id
     * @return the perspective by id
     */
    PerspectiveResponse getPerspectiveById(UUID id);

    /**
     * Update perspective method.
     *
     * @param request the request
     * @param id      the id
     * @return the perspective response
     */
    PerspectiveResponse updatePerspective(UpdatePerspectiveRequest request, UUID id);

    /**
     * Create perspective method.
     *
     * @param request the request
     * @return the perspective response
     */
    PerspectiveResponse createPerspective(CreatePerspectiveRequest request);

    /**
     * Delete perspective method.
     *
     * @param id the id
     * @return the perspective response
     */
    PerspectiveResponse deletePerspective(UUID id);
}
