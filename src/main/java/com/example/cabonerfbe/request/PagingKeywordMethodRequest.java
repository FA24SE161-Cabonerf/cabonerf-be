package com.example.cabonerfbe.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * The class Paging keyword method request.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingKeywordMethodRequest extends PaginationRequest {
    String keyword;
    UUID systemBoundaryId;

    /**
     * Instantiates a new Paging keyword method request.
     *
     * @param pageCurrent      the page current
     * @param pageSize         the page size
     * @param systemBoundaryId the system boundary id
     * @param keyword          the keyword
     */
    public PagingKeywordMethodRequest(int pageCurrent, int pageSize, UUID systemBoundaryId, String keyword) {
        this.setPageSize(pageSize);
        this.setKeyword(keyword);
        this.setSystemBoundaryId(systemBoundaryId);
        this.setCurrentPage(pageCurrent);
    }
}
