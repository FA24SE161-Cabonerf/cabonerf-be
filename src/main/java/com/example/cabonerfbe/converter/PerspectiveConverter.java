package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.models.Perspective;
import com.example.cabonerfbe.response.PerspectiveResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Perspective converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface PerspectiveConverter {
    /**
     * The constant INSTANCE.
     */
    PerspectiveConverter INSTANCE = Mappers.getMapper(PerspectiveConverter.class);

    /**
     * From perspective to perspective dto method.
     *
     * @param perspective the perspective
     * @return the perspective response
     */
    PerspectiveResponse fromPerspectiveToPerspectiveDto(Perspective perspective);

    /**
     * From list perspective to list perspective response method.
     *
     * @param perspective the perspective
     * @return the list
     */
    List<PerspectiveResponse> fromListPerspectiveToListPerspectiveResponse(List<Perspective> perspective);


}
