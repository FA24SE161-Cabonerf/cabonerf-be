package com.example.cabonerfbe.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * The class Import emission substance request.
 *
 * @author SonPHH.
 */
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportEmissionSubstanceRequest {
    private String lifeCycleImpactAssessmentMethodName;
}
