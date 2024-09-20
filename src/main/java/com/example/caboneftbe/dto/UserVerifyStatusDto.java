package com.example.caboneftbe.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserVerifyStatusDto implements Serializable {
    long id;
    String statusName;
    String description;
}
