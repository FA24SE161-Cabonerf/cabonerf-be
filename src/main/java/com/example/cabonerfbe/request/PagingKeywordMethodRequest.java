package com.example.cabonerfbe.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingKeywordMethodRequest extends PaginationRequest {
    String keyword;
    UUID systemBoundaryId;

    public PagingKeywordMethodRequest(int pageCurrent, int pageSize, UUID systemBoundaryId, String keyword) {
        this.setPageSize(pageSize);
        this.setKeyword(keyword);
        this.setSystemBoundaryId(systemBoundaryId);
        this.setCurrentPage(pageCurrent);
    }
}
