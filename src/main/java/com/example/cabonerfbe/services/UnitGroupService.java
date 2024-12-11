package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UnitGroupDto;
import com.example.cabonerfbe.request.CreateUnitGroupRequest;
import com.example.cabonerfbe.request.UpdateUnitGroupRequest;
import com.example.cabonerfbe.response.UnitGroupResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Unit group service.
 *
 * @author SonPHH.
 */
public interface UnitGroupService {
    /**
     * Gets all unit group.
     *
     * @return the all unit group
     */
    List<UnitGroupDto> getAllUnitGroup();

    /**
     * Gets unit group by id.
     *
     * @param id the id
     * @return the unit group by id
     */
    UnitGroupDto getUnitGroupById(UUID id);

    /**
     * Create unit group method.
     *
     * @param request the request
     * @return the unit group response
     */
    UnitGroupResponse createUnitGroup(CreateUnitGroupRequest request);

    /**
     * Update unit group by id method.
     *
     * @param groupId the group id
     * @param request the request
     * @return the unit group response
     */
    UnitGroupResponse updateUnitGroupById(UUID groupId, UpdateUnitGroupRequest request);

    /**
     * Delete unit group by id method.
     *
     * @param groupId the group id
     * @return the unit group response
     */
    UnitGroupResponse deleteUnitGroupById(UUID groupId);
}
