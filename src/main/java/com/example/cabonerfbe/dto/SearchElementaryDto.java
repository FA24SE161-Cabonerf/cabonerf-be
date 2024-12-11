package com.example.cabonerfbe.dto;

import lombok.*;

/**
 * The class Search elementary dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SearchElementaryDto {
    /**
     * The Emission substance.
     */
    SearchEmissionSubstanceDto emissionSubstance;
}
