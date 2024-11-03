package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.models.LifeCycleStage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LifeCycleStageConverter {
    LifeCycleStageConverter INSTANCE = Mappers.getMapper(LifeCycleStageConverter.class);

    LifeCycleStageDto fromLifecycleStageToLifecycleStageDto(LifeCycleStage lifecycleStage);
    List<LifeCycleStageDto> fromListLifecycleStageToLifecycleStageDto(List<LifeCycleStage> lifecycleStage);


}
