package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.response.CreateElementaryResponse;
import com.example.cabonerfbe.response.SearchElementaryResponse;

import java.util.UUID;

public interface ExchangesService {

    CreateElementaryResponse createElementaryExchanges(CreateElementaryRequest request);

    CreateElementaryResponse createProductExchanges(CreateProductRequest request);

    SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId, UUID emissionCompartmentId, UUID impactCategoryId);
}
