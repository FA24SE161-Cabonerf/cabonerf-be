package com.example.caboneftbe.response;

import com.example.caboneftbe.dto.ExchangesDto;
import com.example.caboneftbe.dto.ProcessDto;
import com.example.caboneftbe.dto.ProcessImpactValueDto;
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
public class CreateProcessResponse {
    private ProcessDto process;
    private List<ProcessImpactValueDto> impactValues;
    private List<ExchangesDto> exchanges;
}
