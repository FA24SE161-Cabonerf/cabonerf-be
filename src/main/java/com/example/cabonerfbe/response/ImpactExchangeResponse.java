package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import lombok.*;

import java.util.List;

/**
 * The class Impact exchange response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ImpactExchangeResponse {
    private List<ProcessImpactValueDto> impacts;
    private ExchangesDto exchange;
}
