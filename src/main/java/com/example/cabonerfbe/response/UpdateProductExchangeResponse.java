package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ExchangesDto;
import lombok.*;

import java.util.UUID;

/**
 * The class Update product exchange response.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UpdateProductExchangeResponse {
    private UUID processId;
    private ExchangesDto exchange;
}
