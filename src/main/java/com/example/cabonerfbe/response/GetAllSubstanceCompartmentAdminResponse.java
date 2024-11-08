package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetAllSubstanceCompartmentAdminResponse {
    private int pageCurrent;
    private int pageSize;
    private int totalPage;
    List<EmissionSubstanceDto> list;
}
