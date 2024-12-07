package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.GetAllProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProcessService {
    ProcessDetailDto createProcess(CreateProcessRequest request);
    ProcessDto getProcessById(UUID id);
    List<ProcessDto> getAllProcesses(UUID projectId);
    ProcessDetailDto updateProcess(UUID id, UpdateProcessRequest request);
    String deleteProcess(UUID id);
}
