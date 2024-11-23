package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreateUnitRequest;
import com.example.cabonerfbe.request.UpdateUnitRequest;
import com.example.cabonerfbe.response.UnitResponse;

import java.util.List;
import java.util.UUID;

public interface UnitService {
    List<UnitResponse> getAllUnitsFromGroupId(UUID id);

    UnitResponse createUnitInUnitGroup(UUID groupId, CreateUnitRequest request);

    List<UnitResponse> getAllUnits();

    UnitResponse getUnitById(UUID unitId);

    UnitResponse updateUnitById(UUID unitId, UpdateUnitRequest request);

    UnitResponse deleteUnitById(UUID unitId);

}
