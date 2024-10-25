package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.PerspectiveConverter;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Perspective;
import com.example.cabonerfbe.repositories.PerspectiveRepository;
import com.example.cabonerfbe.response.PerspectiveResponse;
import com.example.cabonerfbe.services.PerspectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerspectiveServiceImpl implements PerspectiveService {
    @Autowired
    PerspectiveRepository perspectiveRepository;
    @Autowired
    PerspectiveConverter perspectiveConverter;

    @Override
    public List<PerspectiveResponse> getAllPerspective() {
        List<Perspective> perspective = perspectiveRepository.findByStatus(Constants.STATUS_TRUE);
        if (perspective.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_PERSPECTIVE_FOUND);
        }
        return perspectiveConverter.fromListPerspectiveToListPerspectiveResponse(perspective);
    }
}
