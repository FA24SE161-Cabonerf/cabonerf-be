package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.PerspectiveDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImpactMethodResponse {
    private Long id;
    private String name;
    private String description;
    private String version;
    private String reference;
    private PerspectiveDto perspective;
}
