package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Method dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MethodDto {
    private UUID id;
    private String name;
    private String version;
    private PerspectiveDto perspective;
}
