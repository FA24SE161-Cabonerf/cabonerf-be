package com.example.cabonerfbe.services;

import com.example.cabonerfbe.models.Unit;
import com.example.cabonerfbe.request.CreateUnitRequest;
import com.example.cabonerfbe.request.UpdateUnitRequest;
import com.example.cabonerfbe.response.UnitResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The interface Unit service.
 *
 * @author SonPHH.
 */
public interface UnitService {
    /**
     * Gets all units from group id.
     *
     * @param id the id
     * @return the all units from group id
     */
    List<UnitResponse> getAllUnitsFromGroupId(UUID id);

    /**
     * Create unit in unit group method.
     *
     * @param groupId the group id
     * @param request the request
     * @return the unit response
     */
    UnitResponse createUnitInUnitGroup(UUID groupId, CreateUnitRequest request);

    /**
     * Gets all units.
     *
     * @return the all units
     */
    List<UnitResponse> getAllUnits();

    /**
     * Gets unit by id.
     *
     * @param unitId the unit id
     * @return the unit by id
     */
    UnitResponse getUnitById(UUID unitId);

    /**
     * Update unit by id method.
     *
     * @param unitId  the unit id
     * @param request the request
     * @return the unit response
     */
    UnitResponse updateUnitById(UUID unitId, UpdateUnitRequest request);

    /**
     * Delete unit by id method.
     *
     * @param unitId the unit id
     * @return the unit response
     */
    UnitResponse deleteUnitById(UUID unitId);

    BigDecimal convertValue(Unit originalUnit, BigDecimal originalValue, Unit targetUnit);

}
