package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.IndustryCodeDto;
import com.example.cabonerfbe.models.ImpactCategory;
import com.example.cabonerfbe.models.IndustryCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IndustryCodeConverter {
    IndustryCodeConverter INSTANCE = Mappers.getMapper(IndustryCodeConverter.class);

    IndustryCodeDto modelToDto(IndustryCode industryCode);
}
