package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.cabonerfbe.dto.MidpointSubstanceFactorsDto;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import com.example.cabonerfbe.response.MidpointImpactCharacterizationFactorsResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MidpointImpactCharacterizationFactorConverter {
    MidpointImpactCharacterizationFactorConverter INSTANCE = Mappers.getMapper(MidpointImpactCharacterizationFactorConverter.class);

    MidpointImpactCharacterizationFactorsDto fromMidpointToMidpointDto(MidpointImpactCharacterizationFactors midpointImpactCharacterizationFactors);

    List<MidpointImpactCharacterizationFactorsDto> fromMidpointListToMidpointDtoList(List<MidpointImpactCharacterizationFactors> impactCharacterizationFactors);

    List<MidpointImpactCharacterizationFactorsResponse> fromMidpointDtoListToMidpointResponseList(List<MidpointImpactCharacterizationFactorsDto> midpointImpactCharacterizationFactorsDtos);

    MidpointImpactCharacterizationFactorsResponse fromMidpointDtoToMidpointResponse(MidpointImpactCharacterizationFactorsDto midpointImpactCharacterizationFactorsDto);

    default MidpointSubstanceFactorsDto fromQueryResultsToDto(Object[] result) {
        MidpointSubstanceFactorsDto dto = new MidpointSubstanceFactorsDto();
        dto.setId((Long) result[0]);
        dto.setCasNumber((String) result[1]);
        dto.setName((String) result[2]);
        dto.setChemicalName((String) result[3]);
        dto.setMolecularFormula((String) result[4]);
        dto.setAlternativeFormula((String) result[5]);
        dto.setCompartmentName((String) result[6]);
        dto.setIndividualist((Double) result[7]);
        dto.setHierarchist((Double) result[8]);
        dto.setEgalitarian((Double) result[9]);
        return dto;
    }

    PageList<MidpointSubstanceFactorsResponse> fromMidpointSubstanceFactorPageListDtoToMidpointSubstanceFactorPageListResponse(PageList<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoPageList);
}
