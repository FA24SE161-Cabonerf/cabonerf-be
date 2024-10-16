package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ConnectorDto {
    private long id;
    private long startProcessId;
    private long endProcessId;
    private long startExchangesId;
    private long endExchangesId;
}
