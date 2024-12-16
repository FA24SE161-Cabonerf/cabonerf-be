package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ProcessDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
public class ProjectWithProcessResponse {
    private UUID firstProjectId;
    private List<ProcessDto> firstProjectProcesses;
    private UUID secondProjectId;
    private List<ProcessDto> secondProjectProcesses;
}
