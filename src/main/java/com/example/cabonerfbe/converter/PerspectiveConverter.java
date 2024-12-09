package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.models.Perspective;
import com.example.cabonerfbe.response.PerspectiveResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PerspectiveConverter {
    PerspectiveConverter INSTANCE = Mappers.getMapper(PerspectiveConverter.class);

    PerspectiveResponse fromPerspectiveToPerspectiveDto(Perspective perspective);

    List<PerspectiveResponse> fromListPerspectiveToListPerspectiveResponse(List<Perspective> perspective);


}
