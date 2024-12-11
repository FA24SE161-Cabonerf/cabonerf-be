package com.example.cabonerfbe.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The class Base impact method request.
 *
 * @author SonPHH.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseImpactMethodRequest {
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
    @NotNull(message = "Method perspective is required.")
    private UUID perspectiveId;
}
