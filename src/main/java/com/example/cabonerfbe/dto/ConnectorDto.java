package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ConnectorDto {
    private UUID id;
    private UUID startProcessId;
    private UUID endProcessId;
    private UUID startExchangesId;
    private UUID endExchangesId;
}
