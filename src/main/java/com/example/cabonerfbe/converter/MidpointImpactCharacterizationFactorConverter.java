package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.FactorDto;
import com.example.cabonerfbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.cabonerfbe.dto.MidpointSubstanceFactorsDto;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import com.example.cabonerfbe.response.MidpointImpactCharacterizationFactorsResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The interface Midpoint impact characterization factor converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface MidpointImpactCharacterizationFactorConverter {
    /**
     * The constant INSTANCE.
     */
    MidpointImpactCharacterizationFactorConverter INSTANCE = Mappers.getMapper(MidpointImpactCharacterizationFactorConverter.class);

    /**
     * From midpoint to midpoint dto method.
     *
     * @param midpointImpactCharacterizationFactors the midpoint impact characterization factors
     * @return the midpoint impact characterization factors dto
     */
    MidpointImpactCharacterizationFactorsDto fromMidpointToMidpointDto(MidpointImpactCharacterizationFactors midpointImpactCharacterizationFactors);

    /**
     * From midpoint list to midpoint dto list method.
     *
     * @param impactCharacterizationFactors the impact characterization factors
     * @return the list
     */
    List<MidpointImpactCharacterizationFactorsDto> fromMidpointListToMidpointDtoList(List<MidpointImpactCharacterizationFactors> impactCharacterizationFactors);

    /**
     * From midpoint dto list to midpoint response list method.
     *
     * @param midpointImpactCharacterizationFactorsDtos the midpoint impact characterization factors dtos
     * @return the list
     */
    List<MidpointImpactCharacterizationFactorsResponse> fromMidpointDtoListToMidpointResponseList(List<MidpointImpactCharacterizationFactorsDto> midpointImpactCharacterizationFactorsDtos);

    /**
     * From midpoint dto to midpoint response method.
     *
     * @param midpointImpactCharacterizationFactorsDto the midpoint impact characterization factors dto
     * @return the midpoint impact characterization factors response
     */
    MidpointImpactCharacterizationFactorsResponse fromMidpointDtoToMidpointResponse(MidpointImpactCharacterizationFactorsDto midpointImpactCharacterizationFactorsDto);

    /**
     * From query results to dto method.
     *
     * @param result the result
     * @return the midpoint substance factors dto
     */
    default MidpointSubstanceFactorsDto fromQueryResultsToDto(Object[] result) {
        MidpointSubstanceFactorsDto dto = new MidpointSubstanceFactorsDto();
        dto.setId((UUID) result[0]);
        dto.setCasNumber((String) result[1]);
        dto.setName((String) result[2]);
        dto.setChemicalName((String) result[3]);
        dto.setMolecularFormula((String) result[4]);
        dto.setAlternativeFormula((String) result[5]);
        dto.setCompartmentName((String) result[6]);
        dto.setIndividualist((BigDecimal) result[7]);
        dto.setHierarchist((BigDecimal) result[8]);
        dto.setEgalitarian((BigDecimal) result[9]);
        return dto;
    }


    /**
     * From midpoint substance factor page list dto to midpoint substance factor page list response method.
     *
     * @param midpointSubstanceFactorsDtoPageList the midpoint substance factors dto page list
     * @return the page list
     */
    PageList<MidpointSubstanceFactorsResponse> fromMidpointSubstanceFactorPageListDtoToMidpointSubstanceFactorPageListResponse(PageList<MidpointSubstanceFactorsDto> midpointSubstanceFactorsDtoPageList);

    /**
     * From midpoint list to midpoint response list method.
     *
     * @param factors the factors
     * @return the list
     */
    List<MidpointImpactCharacterizationFactorsResponse> fromMidpointListToMidpointResponseList(List<MidpointImpactCharacterizationFactors> factors);

    /**
     * From midpoint to midpoint response method.
     *
     * @param factor the factor
     * @return the midpoint impact characterization factors response
     */
    MidpointImpactCharacterizationFactorsResponse fromMidpointToMidpointResponse(MidpointImpactCharacterizationFactors factor);

    /**
     * From midpoint to factor method.
     *
     * @param factor the factor
     * @return the factor dto
     */
    FactorDto fromMidpointToFactor(MidpointImpactCharacterizationFactors factor);
}
