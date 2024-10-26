package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class EmissionCompartmentDto {
    private UUID id;
    private String name;
    private String description;
}
