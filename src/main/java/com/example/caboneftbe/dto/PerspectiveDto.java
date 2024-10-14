package com.example.caboneftbe.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
