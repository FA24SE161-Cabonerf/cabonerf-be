package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.models.SubstancesCompartments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SearchElementaryResponse {
    private int pageCurrent;
    private int pageSize;
    private int totalPage;
    List<SearchSubstancesCompartmentsDto> list;
}
