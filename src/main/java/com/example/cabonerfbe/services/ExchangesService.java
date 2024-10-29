package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.request.SearchElementaryRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;
import com.example.cabonerfbe.response.SearchElementaryResponse;

import java.util.List;
import java.util.UUID;

public interface ExchangesService {

    CreateProcessResponse createElementaryExchanges(CreateElementaryRequest request);

    CreateProcessResponse createProductExchanges(CreateProductRequest request);

    List<SearchElementaryResponse> search(int pageCurrent, int pageSize, String keyWord, UUID methodId, UUID emissionCompartmentId, UUID impactCategoryId);
}
