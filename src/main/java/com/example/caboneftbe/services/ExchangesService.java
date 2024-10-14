package com.example.caboneftbe.services;

import com.example.caboneftbe.request.CreateElementaryRequest;
import com.example.caboneftbe.request.CreateProductRequest;
import com.example.caboneftbe.response.CreateProcessResponse;

public interface ExchangesService {

    CreateProcessResponse createElementaryExchanges(CreateElementaryRequest request);

    CreateProcessResponse createProductExchanges(CreateProductRequest request);
}
