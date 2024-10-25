package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImpactMethodCategoryResponse {
    ImpactMethodResponse impactMethod;
    ImpactCategoryDto impactCategory;
}
