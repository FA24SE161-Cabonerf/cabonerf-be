package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.LifeCycleBreakdownDto;
import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.models.LifeCycleStage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LifeCycleStageConverter {
    LifeCycleStageConverter INSTANCE = Mappers.getMapper(LifeCycleStageConverter.class);

    LifeCycleStageDto fromLifecycleStageToLifecycleStageDto(LifeCycleStage lifecycleStage);

    List<LifeCycleStageDto> fromListLifecycleStageToLifecycleStageDto(List<LifeCycleStage> lifecycleStage);

    LifeCycleBreakdownDto toPercent(LifeCycleStage lifeCycleStage);
}
