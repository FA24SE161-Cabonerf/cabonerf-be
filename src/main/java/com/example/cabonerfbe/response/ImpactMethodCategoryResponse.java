package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class Impact method category response.
 *
 * @author SonPHH.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImpactMethodCategoryResponse {
    /**
     * The Impact method.
     */
    ImpactMethodResponse impactMethod;
    /**
     * The Impact category.
     */
    ImpactCategoryDto impactCategory;
}
