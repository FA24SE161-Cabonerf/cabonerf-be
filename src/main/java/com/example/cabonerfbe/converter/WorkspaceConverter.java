package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UserDto;
import com.example.cabonerfbe.dto.WorkspaceDto;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.models.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkspaceConverter {
    WorkspaceConverter INSTANCE = Mappers.getMapper(WorkspaceConverter.class);

    WorkspaceDto fromWorkspaceToWorkspaceDto(Workspace workspace);
}
