package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetImpactCategoryListResponse {
    List<ImpactCategoryDto> impactCategoryList;
}
