package com.example.cabonerfbe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemBoundaryDto {
    private String boundaryFrom;
    private String boundaryTo;
    private String description;
}
