package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MethodDto {
    private long id;
    private String name;
    private String version;
    private PerspectiveDto perspective;
}
