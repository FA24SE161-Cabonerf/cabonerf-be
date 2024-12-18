package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class Change password request.
 *
 * @author SonPHH.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    /**
     * The Old password.
     */
    @NotBlank(message = "Old password cannot be empty")
    String oldPassword;
    /**
     * The New password.
     */
    @NotBlank(message = "New password cannot be empty")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 1 lowercase, 1 uppercase, 1 number and 1 symbol")
    String newPassword;

    /**
     * The New password confirm.
     */
    String newPasswordConfirm;

}
