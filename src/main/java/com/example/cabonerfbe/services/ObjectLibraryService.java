package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.SearchObjectLibraryDto;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.request.AddObjectLibraryRequest;
import com.example.cabonerfbe.request.PagingKeywordMethodRequest;
import com.example.cabonerfbe.request.RemoveObjectLibraryRequest;
import com.example.cabonerfbe.response.DeleteProcessResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Object library service.
 *
 * @author SonPHH.
 */
public interface ObjectLibraryService {

    /**
     * Search object library of organization method.
     *
     * @param userId         the user id
     * @param organizationId the organization id
     * @param request        the request
     * @return the search object library dto
     */
    SearchObjectLibraryDto searchObjectLibraryOfOrganization(UUID userId, UUID organizationId, PagingKeywordMethodRequest request);

    /**
     * Remove from object library method.
     *
     * @param userId the user id
     * @param id     the id
     * @return the delete process response
     */
    DeleteProcessResponse removeFromObjectLibrary(UUID userId, UUID organizationId, RemoveObjectLibraryRequest request);

    /**
     * Save to object library method.
     *
     * @param userId    the user id
     * @param processId the process id
     * @return the list
     */
    List<Process> saveToObjectLibrary(UUID userId, UUID processId);

    /**
     * Add from object library to project method.
     *
     * @param request the request
     * @return the process dto
     */
    ProcessDto addFromObjectLibraryToProject(AddObjectLibraryRequest request);
}
