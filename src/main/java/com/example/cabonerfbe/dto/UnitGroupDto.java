package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitGroupDto {
    private long id;
    private String name;
    private String unitGroupType;
}
