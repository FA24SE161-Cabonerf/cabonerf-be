package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserInfoRequest {
    @NotEmpty
    String fullName;
    @NotEmpty
    String phone;
    String profilePictureUrl;
    @NotEmpty
    String bio;
}
