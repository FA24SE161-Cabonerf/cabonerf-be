package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.PagingKeywordMethodRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ObjectLibraryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.OBJECT_LIBRARY)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ObjectLibraryController {
    @Autowired
    private ObjectLibraryService objectLibraryService;

    @GetMapping(API_PARAMS.SEARCH_OBJECT_LIBRARY_OF_ORGANIZATION)
    public ResponseEntity<ResponseObject> searchObjectLibraryOfOrganization(@PathVariable UUID organizationId,
                                                                            @RequestParam(defaultValue = "1") int pageCurrent,
                                                                            @RequestParam(defaultValue = "5") int pageSize,
                                                                            @RequestParam UUID methodId,
                                                                            @RequestParam(required = false) String keyword,
                                                                            @RequestHeader(value = Constants.USER_ID_HEADER) UUID userId) {
        log.info("Start searchObjectLibraryOfOrganization methodId: {}, keyword: {}, orgId: {}", methodId, keyword, organizationId);
        PagingKeywordMethodRequest request = new PagingKeywordMethodRequest(pageCurrent, pageSize, methodId, keyword);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.SEARCH_OBJECT_LIBRARY_OF_ORGANIZATION_SUCCESS, objectLibraryService.searchObjectLibraryOfOrganization(userId, organizationId, request))
        );
    }

    @PostMapping(API_PARAMS.SAVE_PROCESS_TO_OBJECT_LIBRARY)
    public ResponseEntity<ResponseObject> saveToObjectLibrary(@RequestHeader(value = Constants.USER_ID_HEADER) UUID userId, @PathVariable UUID processId) {
        log.info("Start saveToObjectLibrary. processId: {}", processId);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.SAVE_TO_OBJECT_LIBRARY_SUCCESS, objectLibraryService.saveToObjectLibrary(userId, processId)
                )
        );
    }

    @DeleteMapping(API_PARAMS.REMOVE_PROCESS_FROM_OBJECT_LIBRARY)
    public ResponseEntity<ResponseObject> removeFromObjectLibrary(@RequestHeader(value = Constants.USER_ID_HEADER) UUID userId, @PathVariable UUID processId) {
        log.info("Start removeFromObjectLibrary. processId: {}", processId);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.REMOVE_FROM_OBJECT_LIBRARY_SUCCESS, objectLibraryService.removeFromObjectLibrary(userId, processId)
                )
        );
    }

    @PostMapping(API_PARAMS.ADD_FROM_OBJECT_LIBRARY_TO_PROJECT)
    public ResponseEntity<ResponseObject> addFromObjectLibraryToProject(@RequestHeader(value = Constants.USER_ID_HEADER) UUID userId, @PathVariable UUID processId, @PathVariable UUID projectId) {
        log.info("Start addFromObjectLibraryToProject. processId: {}", processId);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.ADD_FROM_LIBRARY_TO_PROJECT_SUCCESS, objectLibraryService.addFromObjectLibraryToProject(userId, processId, projectId)
                )
        );
    }


}
