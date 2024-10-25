package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class GetProjectByIdDto {
    private long id;
    private String name;
    private String description;
    private String location;
    private MethodDto method;
    private List<ProjectImpactDto> impacts;
    private List<ProcessDto> processes;
    private List<ConnectorDto> connectors;
}
