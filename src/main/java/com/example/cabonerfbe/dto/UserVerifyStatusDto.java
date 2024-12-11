package com.example.cabonerfbe.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * The class User verify status dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserVerifyStatusDto implements Serializable {
    /**
     * The Id.
     */
    UUID id;
    /**
     * The Status name.
     */
    String statusName;
    /**
     * The Description.
     */
    String description;
}
