package com.example.cabonerfbe.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectDto {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private MethodDto method;
    private LocalDate modifiedAt;
    private OwnerDto owner;
    private WorkspaceDto workspace;
    private List<ProjectImpactDto> impacts;
}
