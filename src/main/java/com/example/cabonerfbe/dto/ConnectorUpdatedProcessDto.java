package com.example.cabonerfbe.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

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
