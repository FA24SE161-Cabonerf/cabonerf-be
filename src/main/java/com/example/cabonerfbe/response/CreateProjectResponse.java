package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ProjectDto;
import com.example.cabonerfbe.dto.ProjectImpactValueDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProjectResponse {
    private UUID projectId;
}
