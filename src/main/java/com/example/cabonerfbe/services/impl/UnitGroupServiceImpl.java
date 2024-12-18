package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UnitGroupConverter;
import com.example.cabonerfbe.dto.UnitGroupDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.UnitGroup;
import com.example.cabonerfbe.repositories.UnitGroupRepository;
import com.example.cabonerfbe.request.CreateUnitGroupRequest;
import com.example.cabonerfbe.request.UpdateUnitGroupRequest;
import com.example.cabonerfbe.response.UnitGroupResponse;
import com.example.cabonerfbe.services.UnitGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The class Unit group service.
 *
 * @author SonPHH.
 */
@Service
public class UnitGroupServiceImpl implements UnitGroupService {

    @Autowired
    private UnitGroupRepository unitGroupRepository;
    @Autowired
    private UnitGroupConverter unitGroupConverter;

    @Override
    public List<UnitGroupDto> getAllUnitGroup() {
        List<UnitGroup> unitGroupList = unitGroupRepository.findAllByStatus(Constants.STATUS_TRUE);
        if (unitGroupList.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_UNIT_GROUP_FOUND);
        }
        return unitGroupConverter.fromListUnitGroupToUnitGroupDto(unitGroupList);
    }

    @Override
    public UnitGroupDto getUnitGroupById(UUID id) {
        UnitGroup unitGroup = unitGroupRepository.findByIdAndStatus(id, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.GET_UNIT_GROUP_BY_ID_NOT_FOUND)
        );
        return unitGroupConverter.fromUnitGroupToUnitGroupDto(unitGroup);
    }

    @Override
    public UnitGroupResponse createUnitGroup(CreateUnitGroupRequest request) {
        UnitGroup unitGroup = unitGroupRepository.findByNameAndUnitGroupTypeAndStatus(request.getUnitGroupName(), request.getUnitGroupType(), true);
        if (unitGroup != null) {
            throw CustomExceptions.badRequest(MessageConstants.UNIT_GROUP_EXIST);
        }
        UnitGroup newUnitGroup = new UnitGroup();
        newUnitGroup.setName(request.getUnitGroupName());
        newUnitGroup.setUnitGroupType(request.getUnitGroupType());

        return unitGroupConverter.fromUnitGroupToUnitGroupResponse(unitGroupRepository.save(newUnitGroup));
    }

    @Override
    public UnitGroupResponse updateUnitGroupById(UUID groupId, UpdateUnitGroupRequest request) {
        UnitGroup unitGroup = unitGroupRepository.findByIdAndStatus(groupId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_UNIT_GROUP_FOUND)
        );

        if (unitGroupRepository.checkExist(groupId, request.getUnitGroupName()).isPresent()) {
            throw CustomExceptions.badRequest(MessageConstants.UNIT_GROUP_NAME_EXIST);
        }

        unitGroup.setName(request.getUnitGroupName());
        unitGroup.setUnitGroupType(request.getUnitGroupType());
        return unitGroupConverter.fromUnitGroupToUnitGroupResponse(unitGroupRepository.save(unitGroup));
    }

    @Override
    public UnitGroupResponse deleteUnitGroupById(UUID groupId) {
        UnitGroup unitGroup = unitGroupRepository.findByIdAndStatus(groupId, Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_UNIT_GROUP_FOUND)
        );

        unitGroup.setStatus(Constants.STATUS_FALSE);
        return unitGroupConverter.fromUnitGroupToUnitGroupResponse(unitGroupRepository.save(unitGroup));
    }
}
