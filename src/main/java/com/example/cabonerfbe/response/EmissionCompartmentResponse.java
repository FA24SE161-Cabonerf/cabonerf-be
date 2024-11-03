package com.example.cabonerfbe.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmissionCompartmentResponse {
    private UUID id;
    private String name;
    private String description;

}
