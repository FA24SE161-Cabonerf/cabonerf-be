package com.example.cabonerfbe.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemBoundaryResponse {
    private UUID id;
    private String boundaryFrom;
    private String boundaryTo;
    private String description;
}
