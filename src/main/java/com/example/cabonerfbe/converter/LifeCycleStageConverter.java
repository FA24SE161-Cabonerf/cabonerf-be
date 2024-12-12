package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.LifeCycleBreakdownDto;
import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.dto.LifeCycleStagePercentDto;
import com.example.cabonerfbe.models.LifeCycleStage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Life cycle stage converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface LifeCycleStageConverter {
    /**
     * The constant INSTANCE.
     */
    LifeCycleStageConverter INSTANCE = Mappers.getMapper(LifeCycleStageConverter.class);

    /**
     * From lifecycle stage to lifecycle stage dto method.
     *
     * @param lifecycleStage the lifecycle stage
     * @return the life cycle stage dto
     */
    LifeCycleStageDto fromLifecycleStageToLifecycleStageDto(LifeCycleStage lifecycleStage);

    /**
     * From list lifecycle stage to lifecycle stage dto method.
     *
     * @param lifecycleStage the lifecycle stage
     * @return the list
     */
    List<LifeCycleStageDto> fromListLifecycleStageToLifecycleStageDto(List<LifeCycleStage> lifecycleStage);

    /**
     * To percent method.
     *
     * @param lifeCycleStage the life cycle stage
     * @return the life cycle breakdown dto
     */
    LifeCycleBreakdownDto toPercent(LifeCycleStage lifeCycleStage);

    /**
     * To percent get all method.
     *
     * @param lifeCycleStage the life cycle stage
     * @return the life cycle stage percent dto
     */
    LifeCycleStagePercentDto toPercentGetAll(LifeCycleStage lifeCycleStage);
}
