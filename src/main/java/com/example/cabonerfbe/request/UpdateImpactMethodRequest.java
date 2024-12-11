package com.example.cabonerfbe.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The class Update impact method request.
 *
 * @author SonPHH.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateImpactMethodRequest {
    @NotBlank(message = "Method name is required.")
    private String name;
    @NotBlank(message = "Method description is required.")
    private String description;
    @NotBlank(message = "Method version is required.")
    @Pattern(regexp = "^(\\d{4}(\\sv(\\d+)(\\.\\d+){1,2})?|v(\\d+)(\\.\\d+){1,2})$",
            message = "Version must follow formats 'YYYY', 'vX.Y', 'vX.Y.Z', or 'YYYY vX.Y.Z'.")
    private String version;
    @Nullable
    private String reference;
    @NotEmpty(message = "Method perspective is required.")
    private UUID perspectiveId;
}
