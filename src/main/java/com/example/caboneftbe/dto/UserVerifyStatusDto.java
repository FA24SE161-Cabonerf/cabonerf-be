package com.example.caboneftbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserVerifyStatusDto {
    long id;
    String statusName;
    String description;
}
