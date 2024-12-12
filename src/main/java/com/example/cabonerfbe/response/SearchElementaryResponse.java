package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.SearchEmissionSubstanceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The class Search elementary response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SearchElementaryResponse {
    /**
     * The List.
     */
    List<SearchEmissionSubstanceDto> list;
    private int pageCurrent;
    private int pageSize;
    private int totalPage;
}
