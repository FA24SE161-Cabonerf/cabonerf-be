package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Life cycle stage dto.
 *
 * @author SonPHH.
 */
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
