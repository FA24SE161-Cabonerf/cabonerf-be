package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PathDto {
    private String destinationNodeId; // Destination node ID
    private BigDecimal net;
    private List<String> path;
}
