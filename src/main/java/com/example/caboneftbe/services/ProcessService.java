package com.example.caboneftbe.services;

import com.example.caboneftbe.request.CreateProcessRequest;
import com.example.caboneftbe.response.CreateProcessResponse;

public interface ProcessService {
    CreateProcessResponse createProcess(CreateProcessRequest request);
}
