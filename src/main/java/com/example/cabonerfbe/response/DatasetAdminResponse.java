package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.DatasetDto;
import com.example.cabonerfbe.dto.ProcessDetailDto;
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
public class DatasetAdminResponse {
    private int pageCurrent;
    private int pageSize;
    private int totalPage;
    private List<DatasetDto> data;
}