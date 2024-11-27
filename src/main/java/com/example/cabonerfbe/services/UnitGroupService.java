package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UnitGroupDto;
import com.example.cabonerfbe.request.CreateUnitGroupRequest;
import com.example.cabonerfbe.request.UpdateUnitGroupRequest;
import com.example.cabonerfbe.response.UnitGroupResponse;

import java.util.List;
import java.util.UUID;

public interface UnitGroupService {
    List<UnitGroupDto> getAllUnitGroup();

    UnitGroupDto getUnitGroupById(UUID id);

    UnitGroupResponse createUnitGroup(CreateUnitGroupRequest request);

    UnitGroupResponse updateUnitGroupById(UUID groupId, UpdateUnitGroupRequest request);

    UnitGroupResponse deleteUnitGroupById(UUID groupId);
}
