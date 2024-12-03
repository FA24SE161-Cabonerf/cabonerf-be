package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ObjectLibraryResponse {
    private UUID objectId;
}