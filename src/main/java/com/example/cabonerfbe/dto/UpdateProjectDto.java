package com.example.cabonerfbe.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * The class Update project dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UpdateProjectDto {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private boolean favorite;
    private MethodDto method;
    private LocalDate modifiedAt;
}
