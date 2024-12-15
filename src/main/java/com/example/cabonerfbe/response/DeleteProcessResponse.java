package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

/**
 * The class Delete process response.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class DeleteProcessResponse {
    private UUID id;
}
