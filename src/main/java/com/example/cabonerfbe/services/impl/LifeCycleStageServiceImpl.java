package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.LifeCycleStageConverter;
import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.LifeCycleStage;
import com.example.cabonerfbe.repositories.LifeCycleStageRepository;
import com.example.cabonerfbe.request.LifeCycleStagesRequest;
import com.example.cabonerfbe.services.LifeCycleStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LifeCycleStageServiceImpl implements LifeCycleStageService {
    @Autowired
    private LifeCycleStageRepository repository;
    @Autowired
    private LifeCycleStageConverter converter;

    @Override
    public List<LifeCycleStageDto> getAll() {
        return converter.INSTANCE.fromListLifecycleStageToLifecycleStageDto(repository.findAll());
    }

    @Override
    public LifeCycleStageDto create(LifeCycleStagesRequest request) {
        LifeCycleStage lif = new LifeCycleStage(request.getName(), request.getDescription(), request.getIconUrl());
        return converter.fromLifecycleStageToLifecycleStageDto(repository.save(lif));
    }

    @Override
    public LifeCycleStageDto update(UUID lifeCycleStagesId, LifeCycleStagesRequest request) {
        Optional<LifeCycleStage> lif = repository.findByIdAndStatus(lifeCycleStagesId, true);
        if (lif.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_LIFE_CYCLE_STAGE_FOUND);
        }
        lif.get().setName(request.getName());
        lif.get().setDescription(request.getDescription());
        lif.get().setIconUrl(request.getIconUrl());
        return converter.fromLifecycleStageToLifecycleStageDto(repository.save(lif.get()));
    }

    @Override
    public LifeCycleStageDto delete(UUID lifeCycleStagesId) {
        Optional<LifeCycleStage> lif = repository.findByIdAndStatus(lifeCycleStagesId, true);
        if (lif.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_LIFE_CYCLE_STAGE_FOUND);
        }
        lif.get().setStatus(false);
        return converter.fromLifecycleStageToLifecycleStageDto(repository.save(lif.get()));
    }
}
