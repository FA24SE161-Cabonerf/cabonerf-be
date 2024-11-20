package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SankeyProcessBreakdownDto {
    private UUID id;
    private String name;
    private UUID parentId;
    private double totalFlow;
    private int level;
}
