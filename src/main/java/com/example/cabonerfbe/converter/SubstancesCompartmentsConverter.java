package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.dto.SearchEmissionSubstanceDto;
import com.example.cabonerfbe.models.EmissionSubstance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Substances compartments converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface SubstancesCompartmentsConverter {
    /**
     * The constant INSTANCE.
     */
    SubstancesCompartmentsConverter INSTANCE = Mappers.getMapper(SubstancesCompartmentsConverter.class);

    /**
     * To dto method.
     *
     * @param substancesCompartments the substances compartments
     * @return the search emission substance dto
     */
    SearchEmissionSubstanceDto ToDto(EmissionSubstance substancesCompartments);

    /**
     * Model to dto method.
     *
     * @param substancesCompartments the substances compartments
     * @return the emission substance dto
     */
    EmissionSubstanceDto modelToDto(EmissionSubstance substancesCompartments);


}
