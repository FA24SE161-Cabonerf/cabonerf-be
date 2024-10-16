package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.models.LifeCycleStage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LifecycleStageConverter {
    LifecycleStageConverter INSTANCE = Mappers.getMapper(LifecycleStageConverter.class);

    LifeCycleStageDto fromLifecycleStageToLifecycleStageDto(LifeCycleStage lifecycleStage);
}
