package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.response.SearchElementaryResponse;

import java.util.List;
import java.util.UUID;

public interface ExchangesService {

    ExchangesDto createElementaryExchanges(CreateElementaryRequest request);

    ExchangesDto createProductExchanges(CreateProductRequest request);

    SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId, UUID emissionCompartmentId, UUID impactCategoryId, boolean input);
    List<EmissionSubstanceDto> getAllAdmin(String keyword);

    List<ExchangesDto> removeExchange(UUID exchangeId);
}
