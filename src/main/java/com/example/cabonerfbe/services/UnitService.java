package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.dto.UnitDto;

public interface UnitService {
    PageList<UnitDto> getAllUnit(int currentPage, int pageSize, long unitGroupId);
    UnitDto getById(long id);
}
