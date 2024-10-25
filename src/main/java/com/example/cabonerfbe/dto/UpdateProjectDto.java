package com.example.cabonerfbe.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UpdateProjectDto {
    private long id;
    private String name;
    private String description;
    private String location;
    private MethodDto method;
    private LocalDate modifiedAt;
}
