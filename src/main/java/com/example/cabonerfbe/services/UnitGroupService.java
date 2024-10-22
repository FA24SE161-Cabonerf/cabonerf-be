package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UnitGroupDto;
import com.example.cabonerfbe.request.CreateUnitGroupRequest;
import com.example.cabonerfbe.request.UpdateUnitGroupRequest;
import com.example.cabonerfbe.response.UnitGroupResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface UnitGroupService {
    List<UnitGroupDto> getAllUnitGroup();
    UnitGroupDto getUnitGroupById(long id);

    UnitGroupResponse createUnitGroup(CreateUnitGroupRequest request);

    UnitGroupResponse updateUnitGroupById(Long groupId, UpdateUnitGroupRequest request);

    UnitGroupResponse deleteUnitGroupById(Long groupId);
}
