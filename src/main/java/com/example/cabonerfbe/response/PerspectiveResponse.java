package com.example.cabonerfbe.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerspectiveResponse {
    private long id;
    private String name;
    private String description;
    private String abbr;
}
