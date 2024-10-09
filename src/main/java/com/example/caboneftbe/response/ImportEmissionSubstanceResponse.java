package com.example.caboneftbe.response;

import com.example.caboneftbe.dto.MidpointImpactCharacterizationFactorsDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportEmissionSubstanceResponse {
    private List<MidpointImpactCharacterizationFactorsDto> list;
}
