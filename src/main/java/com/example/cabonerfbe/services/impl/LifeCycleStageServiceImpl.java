package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.LifeCycleStageConverter;
import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.repositories.LifeCycleStageRepository;
import com.example.cabonerfbe.services.LifeCycleStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
