package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class Update unit group request.
 *
 * @author SonPHH.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUnitGroupRequest {
    @NotBlank(message = "Unit group name is required")
    private String unitGroupName;
    @NotBlank(message = "Unit group type is required")
    private String unitGroupType;
}
