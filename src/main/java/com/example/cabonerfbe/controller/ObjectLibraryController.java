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

    @GetMapping(API_PARAMS.GET_OBJECT_LIBRARY_OF_ORGANIZATION)
    public ResponseEntity<ResponseObject> searchObjectLibraryOfOrganization(@PathVariable UUID organizationId,
                                                                            @RequestParam(defaultValue = "1") int pageCurrent,
                                                                            @RequestParam(defaultValue = "5") int pageSize,
                                                                            @RequestParam UUID methodId,
                                                                            @RequestParam(required = false) String keyword) {
        log.info("Start getObjectLibraryOfOrganization methodId: {}, keyword: {}, orgId: {}", methodId, keyword, organizationId);
        PagingKeywordMethodRequest request = new PagingKeywordMethodRequest(pageCurrent, pageSize, methodId, keyword);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_OBJECT_LIBRARY_OF_ORGANIZATION_SUCCESS, objectLibraryService.searchObjectLibraryOfOrganization(organizationId, request))
        );
    }
}
