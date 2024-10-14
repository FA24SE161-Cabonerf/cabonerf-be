package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.Exchanges;
import com.example.caboneftbe.models.Process;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.jetbrains.annotations.NotNull;

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
