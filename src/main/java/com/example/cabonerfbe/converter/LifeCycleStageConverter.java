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
    @Mapping(target = "iconUrl", expression = "java(extractXmlContent(lifecycleStage.getIconUrl()))")
    LifeCycleStageDto fromLifecycleStageToLifecycleStageDto(LifeCycleStage lifecycleStage);
    List<LifeCycleStageDto> fromListLifecycleStageToLifecycleStageDto(List<LifeCycleStage> lifecycleStage);

    default String extractXmlContent(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
            // Trả về nguyên chuỗi hoặc nội dung tùy chọn từ XML
            return xmlContent; // hoặc dùng document.getDocumentElement().getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Xử lý lỗi tùy ý
        }
    }
}
