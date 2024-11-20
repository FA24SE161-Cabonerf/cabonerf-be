package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessNodeDto {
    private Map<String, List<PathDto>> contributions;
}
