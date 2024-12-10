package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatasetResponse {
    private UUID id;
    private String name;
}
