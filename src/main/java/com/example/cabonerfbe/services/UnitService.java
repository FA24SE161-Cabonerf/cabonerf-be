package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.CreateUnitRequest;
import com.example.cabonerfbe.request.UpdateUnitRequest;
import com.example.cabonerfbe.response.UnitResponse;

import java.util.List;

public interface UnitService {
    List<UnitResponse> getAllUnitsFromGroupId(long id);

    UnitResponse createUnitInUnitGroup(Long groupId, CreateUnitRequest request);

    List<UnitResponse> getAllUnits();

    UnitResponse getUnitById(Long unitId);

    UnitResponse updateUnitById(Long unitId, UpdateUnitRequest request);

    UnitResponse deleteUnitById(Long unitId);
}
