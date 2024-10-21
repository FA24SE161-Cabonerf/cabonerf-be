package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.GetAllProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;

import java.util.List;

public interface ProcessService {
    CreateProcessResponse createProcess(CreateProcessRequest request);
    CreateProcessResponse getProcessById(long id);
    List<CreateProcessResponse> getAllProcesses(GetAllProcessRequest request);
    CreateProcessResponse updateProcess(long id, UpdateProcessRequest request);
}
