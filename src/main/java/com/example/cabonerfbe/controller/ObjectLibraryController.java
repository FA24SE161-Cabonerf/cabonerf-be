package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.AddObjectLibraryRequest;
import com.example.cabonerfbe.request.PagingKeywordMethodRequest;
import com.example.cabonerfbe.request.RemoveObjectLibraryRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ObjectLibraryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The class Object library controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.OBJECT_LIBRARY)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ObjectLibraryController {
    @Autowired
    private ObjectLibraryService objectLibraryService;

    /**
     * Search object library of organization method.
     *
     * @param organizationId   the organization id
     * @param pageCurrent      the page current
     * @param pageSize         the page size
     * @param systemBoundaryId the system boundary id
     * @param keyword          the keyword
     * @param userId           the user id
     * @return the response entity
     */
    @GetMapping(API_PARAMS.SEARCH_OBJECT_LIBRARY_OF_ORGANIZATION)
    public ResponseEntity<ResponseObject> searchObjectLibraryOfOrganization(@PathVariable UUID organizationId,
                                                                            @RequestParam(defaultValue = "1") int pageCurrent,
                                                                            @RequestParam(defaultValue = "5") int pageSize,
                                                                            @RequestParam(required = false) UUID systemBoundaryId,
                                                                            @RequestParam(required = false) String keyword,
                                                                            @RequestHeader(value = Constants.USER_ID_HEADER) UUID userId) {
        log.info("Start searchObjectLibraryOfOrganization systemBoundaryId: {}, keyword: {}, orgId: {}", systemBoundaryId, keyword, organizationId);
        PagingKeywordMethodRequest request = new PagingKeywordMethodRequest(pageCurrent, pageSize, systemBoundaryId, keyword);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.SEARCH_OBJECT_LIBRARY_OF_ORGANIZATION_SUCCESS, objectLibraryService.searchObjectLibraryOfOrganization(userId, organizationId, request))
        );
    }

    /**
     * Save to object library method.
     *
     * @param userId    the user id
     * @param processId the process id
     * @return the response entity
     */
    @PostMapping(API_PARAMS.SAVE_PROCESS_TO_OBJECT_LIBRARY)
    public ResponseEntity<ResponseObject> saveToObjectLibrary(@RequestHeader(value = Constants.USER_ID_HEADER) UUID userId, @PathVariable UUID processId) {
        log.info("Start saveToObjectLibrary. processId: {}", processId);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.SAVE_TO_OBJECT_LIBRARY_SUCCESS, objectLibraryService.saveToObjectLibrary(userId, processId)
                )
        );
    }

    /**
     * Remove from object library method.
     *
     * @param userId    the user id
     * @param organizationId the process id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.REMOVE_PROCESS_FROM_OBJECT_LIBRARY)
    public ResponseEntity<ResponseObject> removeFromObjectLibrary(@RequestHeader(value = Constants.USER_ID_HEADER) UUID userId,
                                                                  @PathVariable UUID organizationId,
                                                                  @RequestBody RemoveObjectLibraryRequest request) {
        log.info("Start removeFromObjectLibrary. organizationId: {}, request: {}", organizationId, request);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.REMOVE_FROM_OBJECT_LIBRARY_SUCCESS, objectLibraryService.removeFromObjectLibrary(userId, organizationId, request)
                )
        );
    }

    /**
     * Add from object library to project method.
     *
     * @param userId    the user id
     * @param processId the process id
     * @param projectId the project id
     * @return the response entity
     */
    @PostMapping(API_PARAMS.ADD_FROM_OBJECT_LIBRARY_TO_PROJECT)
    public ResponseEntity<ResponseObject> addFromObjectLibraryToProject(@RequestHeader(value = Constants.USER_ID_HEADER) UUID userId, @PathVariable UUID processId, @PathVariable UUID projectId) {
        log.info("Start addFromObjectLibraryToProject. processId: {}", processId);
        AddObjectLibraryRequest request = new AddObjectLibraryRequest(userId, processId, projectId);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.ADD_FROM_LIBRARY_TO_PROJECT_SUCCESS, objectLibraryService.addFromObjectLibraryToProject(request)
                )
        );
    }


}
