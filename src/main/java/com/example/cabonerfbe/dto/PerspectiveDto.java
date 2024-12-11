package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Perspective dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class PerspectiveDto {
    private UUID id;
    private String name;
    private String abbr;
}
