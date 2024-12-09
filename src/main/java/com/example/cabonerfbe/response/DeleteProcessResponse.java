package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class DeleteProcessResponse {
    private UUID id;
}
