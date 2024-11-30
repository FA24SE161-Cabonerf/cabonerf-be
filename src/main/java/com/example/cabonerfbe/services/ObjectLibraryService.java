package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.SearchObjectLibraryDto;
import com.example.cabonerfbe.request.PagingKeywordMethodRequest;

import java.util.UUID;

public interface ObjectLibraryService {

    SearchObjectLibraryDto searchObjectLibraryOfOrganization(UUID organizationId, PagingKeywordMethodRequest request);
}
