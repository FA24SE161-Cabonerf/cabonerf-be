package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Exchanges type dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ExchangesTypeDto {
    private UUID id;
    private String name;
}
