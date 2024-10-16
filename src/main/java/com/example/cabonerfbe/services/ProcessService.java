package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;

public interface ProcessService {
    CreateProcessResponse createProcess(CreateProcessRequest request);
    CreateProcessResponse getProcessById(long id);
    PageList<CreateProcessResponse> getAllProcesses(int currentPage, int pageSize, long projectId);
}
