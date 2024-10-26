package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.PerspectiveDto;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImpactMethodResponse {
    private UUID id;
    private String name;
    private String description;
    private String version;
    private String reference;
    private PerspectiveDto perspective;
}
