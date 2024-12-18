package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The class Process node dto.
 *
 * @author SonPHH.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessNodeDto {
    private UUID processId;
    private BigDecimal net;
    private List<ProcessNodeDto> subProcesses;
}

