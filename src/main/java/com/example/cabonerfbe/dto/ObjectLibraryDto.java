package com.example.cabonerfbe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The class Object library dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ObjectLibraryDto {
    private UUID id;
    private String name;
    private String description;
    private boolean library;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    private SystemBoundaryDto systemBoundary;
    private LifeCycleStageDto lifeCycleStage;
    private List<ProcessImpactValueDto> impacts;
    private List<ExchangesDto> exchanges;
}
