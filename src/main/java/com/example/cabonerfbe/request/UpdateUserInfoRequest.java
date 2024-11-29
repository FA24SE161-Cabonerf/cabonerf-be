package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserInfoRequest {
    String fullName;
    String phone;
    String profilePictureUrl;
    String bio;
}
