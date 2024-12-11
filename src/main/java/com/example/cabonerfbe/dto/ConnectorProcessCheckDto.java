package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Connector process check dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ConnectorProcessCheckDto {
    private Long processCount;
    private UUID startProjectId;
    private UUID endProjectId;
}
