package com.example.cabonerfbe.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * The class Connector updated process dto.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ConnectorUpdatedProcessDto {
    private UUID processId;
    private ExchangesDto exchange;
}
