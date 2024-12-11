package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.dto.SearchEmissionSubstanceDto;
import com.example.cabonerfbe.models.EmissionSubstance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Emission substance converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface EmissionSubstanceConverter {
    /**
     * The constant INSTANCE.
     */
    EmissionSubstanceConverter INSTANCE = Mappers.getMapper(EmissionSubstanceConverter.class);

    /**
     * To dto method.
     *
     * @param emissionSubstance the emission substance
     * @return the search emission substance dto
     */
    SearchEmissionSubstanceDto ToDto(EmissionSubstance emissionSubstance);

    /**
     * Model to dto method.
     *
     * @param emissionSubstance the emission substance
     * @return the emission substance dto
     */
    EmissionSubstanceDto modelToDto(EmissionSubstance emissionSubstance);

}
