package com.example.caboneftbe.services;

import com.example.caboneftbe.dto.PageList;
import com.example.caboneftbe.request.CreateProcessRequest;
import com.example.caboneftbe.response.CreateProcessResponse;
import com.example.caboneftbe.response.GetAllProcessResponse;

public interface ProcessService {
    CreateProcessResponse createProcess(CreateProcessRequest request);
    CreateProcessResponse getProcessById(long id);
    PageList<CreateProcessResponse> getAllProcesses(int currentPage, int pageSize, long projectId);
}
