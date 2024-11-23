package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.PerspectiveConverter;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Perspective;
import com.example.cabonerfbe.repositories.PerspectiveRepository;
import com.example.cabonerfbe.request.CreatePerspectiveRequest;
import com.example.cabonerfbe.request.UpdatePerspectiveRequest;
import com.example.cabonerfbe.response.PerspectiveResponse;
import com.example.cabonerfbe.services.PerspectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PerspectiveServiceImpl implements PerspectiveService {
    @Autowired
    PerspectiveRepository perspectiveRepository;
    @Autowired
    PerspectiveConverter perspectiveConverter;

    @Override
    public List<PerspectiveResponse> getAllPerspective() {
        List<Perspective> perspective = perspectiveRepository.findByStatus(Constants.STATUS_TRUE);
        return perspectiveConverter.fromListPerspectiveToListPerspectiveResponse(perspective);
    }

    @Override
    public PerspectiveResponse getPerspectiveById(UUID id) {
        Perspective p = perspectiveRepository.findByIdAndStatus(id, true);
        if (p == null) {
            throw CustomExceptions.notFound(MessageConstants.NO_PERSPECTIVE_FOUND);
        }
        return perspectiveConverter.fromPerspectiveToPerspectiveDto(p);
    }

    @Override
    public PerspectiveResponse updatePerspective(UpdatePerspectiveRequest request, UUID id) {
        Perspective p = perspectiveRepository.findByIdAndStatus(id, true);
        if (p == null) {
            throw CustomExceptions.notFound(MessageConstants.NO_PERSPECTIVE_FOUND);
        }
        if (!isNotNullOrBlank(request.getName()) && !isNotNullOrBlank(request.getDescription()) && !isNotNullOrBlank(request.getAbbr())) {
            throw CustomExceptions.badRequest("Update at least one field required");
        }

        if (isNotNullOrBlank(request.getName())) {
            p.setName(request.getName());
        }
        if (isNotNullOrBlank(request.getDescription())) {
            p.setDescription(request.getDescription());
        }
        if (isNotNullOrBlank(request.getAbbr())) {
            p.setAbbr(request.getAbbr());
        }
        return perspectiveConverter.fromPerspectiveToPerspectiveDto(perspectiveRepository.save(p));
    }

    @Override
    public PerspectiveResponse createPerspective(CreatePerspectiveRequest request) {
        Perspective p = new Perspective(request.getName(), request.getDescription(), request.getAbbr());

        return perspectiveConverter.fromPerspectiveToPerspectiveDto(perspectiveRepository.save(p));
    }

    @Override
    public PerspectiveResponse deletePerspective(UUID id) {
        Perspective p = perspectiveRepository.findByIdAndStatus(id, true);
        if (p == null) {
            throw CustomExceptions.notFound(MessageConstants.NO_PERSPECTIVE_FOUND);
        }
        p.setStatus(false);
        return perspectiveConverter.fromPerspectiveToPerspectiveDto(perspectiveRepository.save(p));
    }

    private boolean isNotNullOrBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
