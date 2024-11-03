package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

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
