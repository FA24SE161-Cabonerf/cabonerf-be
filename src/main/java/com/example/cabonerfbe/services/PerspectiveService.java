package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreatePerspectiveRequest;
import com.example.cabonerfbe.request.UpdatePerspectiveRequest;
import com.example.cabonerfbe.response.PerspectiveResponse;

import java.util.List;
import java.util.UUID;

public interface PerspectiveService {
    List<PerspectiveResponse> getAllPerspective();

    PerspectiveResponse getPerspectiveById(UUID id);

    PerspectiveResponse updatePerspective(UpdatePerspectiveRequest request, UUID id);

    PerspectiveResponse createPerspective(CreatePerspectiveRequest request);

    PerspectiveResponse deletePerspective(UUID id);
}
