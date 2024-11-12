package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.OrganizationDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllOrganizationResponse {
    private int pageCurrent;
    private int pageSize;
    private int totalPage;
    private List<OrganizationDto> list;
}
