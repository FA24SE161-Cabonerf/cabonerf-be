package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

/**
 * The class Delete connector response.
 *
 * @author SonPHH.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeleteConnectorResponse {
    /**
     * The Connector id.
     */
    UUID connectorId;
}
