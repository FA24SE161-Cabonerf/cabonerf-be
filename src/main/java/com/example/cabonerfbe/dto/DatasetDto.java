package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The class Dataset dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class DatasetDto {
    private UUID id;
    private String name;
    private SystemBoundaryDto systemBoundary;
    private List<ProcessImpactValueDto> impacts;
}
