package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;

public interface ExchangesService {

    CreateProcessResponse createElementaryExchanges(CreateElementaryRequest request);

    CreateProcessResponse createProductExchanges(CreateProductRequest request);
}
