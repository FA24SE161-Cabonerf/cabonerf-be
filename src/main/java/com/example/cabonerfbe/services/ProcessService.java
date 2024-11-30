package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.DeleteProcessResponse;

import java.util.List;
import java.util.UUID;

public interface ProcessService {
    ProcessDto createProcess(CreateProcessRequest request);

    ProcessDto getProcessById(UUID id);

    List<ProcessDto> getAllProcessesByProjectId(UUID projectId);

    ProcessDetailDto updateProcess(UUID id, UpdateProcessRequest request);

    DeleteProcessResponse deleteProcess(UUID id);

    List<Process> addToObjectLibrary(UUID processId);
}
