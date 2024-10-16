package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class PerspectiveDto {
    private long id;
    private String name;
    private String description;
    private String abbr;
}
