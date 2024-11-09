package com.example.cabonerfbe.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.UUID;
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExchangeRequest {
    private String name;
    @Min(value = 0)
    private Double value;
    private UUID unitId;
    @NotNull
    private UUID processId;
}
