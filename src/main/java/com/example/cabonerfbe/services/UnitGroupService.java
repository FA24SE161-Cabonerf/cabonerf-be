package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UnitGroupDto;

import java.util.List;

public interface UnitGroupService {
    List<UnitGroupDto> getAllUnitGroup();
    UnitGroupDto getUnitGroupById(long id);
}
