package com.example.cabonerfbe.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class Emission substance dashboard response.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmissionSubstanceDashboardResponse {
    private String compartmentName;
    private int count;
}
