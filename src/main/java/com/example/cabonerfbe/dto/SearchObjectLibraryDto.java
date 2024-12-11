package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;

/**
 * The class Search object library dto.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchObjectLibraryDto {
    private List<ObjectLibraryDto> objectLibraryList;
    private int pageCurrent;
    private int pageSize;
    private int totalPage;
}
