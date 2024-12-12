package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The class Get impact category list response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetImpactCategoryListResponse {
    /**
     * The Impact category list.
     */
    List<ImpactCategoryDto> impactCategoryList;
}
