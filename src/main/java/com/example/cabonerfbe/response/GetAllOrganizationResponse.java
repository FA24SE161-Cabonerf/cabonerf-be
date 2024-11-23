package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.OrganizationDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
