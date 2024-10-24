package com.example.cabonerfbe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OwnerDto {
    private long id;
    private String name;
    private String email;
}
