package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Role dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class RoleDto {
    /**
     * The Id.
     */
    UUID id;
    /**
     * The Name.
     */
    String name;
}
