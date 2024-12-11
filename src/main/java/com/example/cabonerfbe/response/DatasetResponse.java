package com.example.cabonerfbe.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * The class Dataset response.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatasetResponse {
    private UUID id;
    private String name;
}
