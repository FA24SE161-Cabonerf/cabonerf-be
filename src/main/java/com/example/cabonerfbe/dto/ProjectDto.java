package com.example.cabonerfbe.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectDto {
    private long id;
    private String name;
    private String description;
    private String location;
    private MethodDto method;
    private LocalDate modifiedAt;
    private OwnerDto owner;
    private WorkspaceDto workspace;
    private List<ProjectImpactDto> impacts;
}
