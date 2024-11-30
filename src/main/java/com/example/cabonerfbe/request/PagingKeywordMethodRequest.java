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
    UUID methodId;

    public PagingKeywordMethodRequest(int pageCurrent, int pageSize, UUID methodId, String keyword) {
        this.setPageSize(pageSize);
        this.setKeyword(keyword);
        this.setMethodId(methodId);
        this.setCurrentPage(pageCurrent);
    }
}
