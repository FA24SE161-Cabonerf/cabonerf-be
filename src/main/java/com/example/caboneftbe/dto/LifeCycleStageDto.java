package com.example.caboneftbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class LifeCycleStageDto {
    private long id;
    private String name;
    private String description;
}
