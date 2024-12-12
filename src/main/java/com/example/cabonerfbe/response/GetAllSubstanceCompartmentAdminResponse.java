package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The class Get all substance compartment admin response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetAllSubstanceCompartmentAdminResponse {
    /**
     * The List.
     */
    List<EmissionSubstanceDto> list;
    private int pageCurrent;
    private int pageSize;
    private int totalPage;
}
