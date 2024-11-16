package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeleteConnectorResponse {
    UUID connectorId;
}
