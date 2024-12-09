package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.request.UpdateExchangeRequest;
import com.example.cabonerfbe.response.ImpactExchangeResponse;
import com.example.cabonerfbe.response.SearchElementaryResponse;
import com.example.cabonerfbe.response.UpdateProductExchangeResponse;

import java.util.List;
import java.util.UUID;

public interface ExchangesService {

    List<ExchangesDto> createElementaryExchanges(CreateElementaryRequest request);

    List<ExchangesDto> createProductExchanges(CreateProductRequest request);

    SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId, UUID emissionCompartmentId, UUID impactCategoryId, boolean input);

    List<EmissionSubstanceDto> getAllAdmin(String keyword);

    ImpactExchangeResponse removeElementaryExchange(UUID exchangeId);

    List<ExchangesDto> removeProductExchange(UUID exchangeId);

    ImpactExchangeResponse updateElementaryExchange(UUID exchangeId, UpdateExchangeRequest request);

    List<UpdateProductExchangeResponse> updateProductExchange(UUID exchangeId, UpdateExchangeRequest request);

}
