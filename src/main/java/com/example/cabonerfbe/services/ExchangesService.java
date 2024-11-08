package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.response.SearchElementaryResponse;

import java.util.List;
import java.util.UUID;

public interface ExchangesService {

    ProcessDto createElementaryExchanges(CreateElementaryRequest request);

    ProcessDto createProductExchanges(CreateProductRequest request);

    SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId, UUID emissionCompartmentId, UUID impactCategoryId, boolean input);
    List<EmissionSubstanceDto> getAllAdmin(String keyword);
}
