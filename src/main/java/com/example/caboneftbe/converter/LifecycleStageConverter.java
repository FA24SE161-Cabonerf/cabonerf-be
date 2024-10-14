package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.LifeCycleStageDto;
import com.example.caboneftbe.models.LifeCycleStage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LifecycleStageConverter {
    LifecycleStageConverter INSTANCE = Mappers.getMapper(LifecycleStageConverter.class);

    LifeCycleStageDto fromLifecycleStageToLifecycleStageDto(LifeCycleStage lifecycleStage);
}
