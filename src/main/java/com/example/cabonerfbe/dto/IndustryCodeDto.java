package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Industry code dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class IndustryCodeDto {
    private UUID id;
    private String code;
    private String name;
}
