package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class LifeCycleBreakdownDto {
    private UUID id;
    private String name;
    private String iconUrl;
    private List<ProcessLifeCycleBreakdownDto> process;
}
