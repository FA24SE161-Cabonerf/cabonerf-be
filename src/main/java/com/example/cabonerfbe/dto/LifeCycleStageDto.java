package com.example.cabonerfbe.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class LifeCycleStageDto {
    private UUID id;
    private String name;
    private String description;
    private String iconUrl;
}
