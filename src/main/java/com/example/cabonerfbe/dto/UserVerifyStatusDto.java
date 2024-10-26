package com.example.cabonerfbe.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserVerifyStatusDto implements Serializable {
    UUID id;
    String statusName;
    String description;
}
