package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.SearchObjectLibraryDto;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.request.PagingKeywordMethodRequest;
import com.example.cabonerfbe.response.DeleteProcessResponse;

import java.util.List;
import java.util.UUID;

public interface ObjectLibraryService {

    SearchObjectLibraryDto searchObjectLibraryOfOrganization(UUID userId, UUID organizationId, PagingKeywordMethodRequest request);
    DeleteProcessResponse removeFromObjectLibrary(UUID userId, UUID id);
    List<Process> saveToObjectLibrary(UUID userId, UUID processId);

    ProcessDto addFromObjectLibraryToProject(UUID userId, UUID processId, UUID projectId);
}
