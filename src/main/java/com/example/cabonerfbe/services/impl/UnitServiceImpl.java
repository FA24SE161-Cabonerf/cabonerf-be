package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UnitConverter;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Unit;
import com.example.cabonerfbe.models.UnitGroup;
import com.example.cabonerfbe.repositories.UnitGroupRepository;
import com.example.cabonerfbe.repositories.UnitRepository;
import com.example.cabonerfbe.request.CreateUnitRequest;
import com.example.cabonerfbe.request.UpdateUnitRequest;
import com.example.cabonerfbe.response.UnitResponse;
import com.example.cabonerfbe.services.UnitService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true)
@SuperBuilder
@AllArgsConstructor
public class UnitServiceImpl implements UnitService {
    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UnitConverter unitConverter;

    @Autowired
    private UnitGroupRepository unitGroupRepository;

    @Override
    public List<UnitResponse> getAllUnits() {
        List<Unit> units = unitRepository.findAllByStatus(Constants.STATUS_TRUE);
        if (units.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND);
        }
        return unitConverter.fromUnitListToUnitResponseList(units);
    }

    @Override
    public UnitResponse getUnitById(UUID unitId) {
        Unit unit = unitRepository.findByIdAndStatus(unitId, Constants.STATUS_TRUE);
        if (unit == null) {
            throw CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND);
        }
        return unitConverter.fromUnitToUnitResponse(unit);
    }

    @Override
    public List<UnitResponse> getAllUnitsFromGroupId(UUID id) {
        List<Unit> units = unitRepository.findAllByStatusAndUnitGroupId(Constants.STATUS_TRUE, id);
        if (units.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND);
        }
        return unitConverter.fromUnitListToUnitResponseList(units);
    }

    @Override
    public UnitResponse createUnitInUnitGroup(UUID groupId, CreateUnitRequest request) {
        UnitGroup unitGroup = unitGroupRepository.findById(groupId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_UNIT_GROUP_FOUND + " id: " + groupId));
        if (unitGroup.getUnitGroupType().equalsIgnoreCase(Constants.UNIT_GROUP_TYPE_NORMAL) && request.getIsDefault() == Constants.IS_DEFAULT_TRUE) {
            if (unitRepository.existsByIsDefaultAndStatusAndUnitGroup(Constants.IS_DEFAULT_TRUE, Constants.STATUS_TRUE, unitGroup))
                throw CustomExceptions.badRequest(MessageConstants.DEFAULT_UNIT_EXIST);
        }
        Unit newUnit = unitConverter.fromUnitRequestToUnit(request);
        newUnit.setUnitGroup(unitGroup);

        return unitConverter.fromUnitToUnitResponse(unitRepository.save(newUnit));
    }

    @Override
    public UnitResponse updateUnitById(UUID unitId, UpdateUnitRequest request) {
        Unit unit = unitRepository.findByIdAndStatus(unitId, Constants.STATUS_TRUE);
        if (unit == null) {
            throw CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND + " id: " + unitId);
        }

        UnitGroup unitGroup = unitGroupRepository.findByIdAndStatus(request.getUnitGroupId(), Constants.STATUS_TRUE);
        if (unitGroup == null) {
            throw CustomExceptions.notFound(MessageConstants.NO_UNIT_GROUP_FOUND + " id: " + request.getUnitGroupId());
        }

        if (request.getIsDefault() == Constants.IS_DEFAULT_TRUE) {
            if (!unit.getIsDefault() && Constants.UNIT_GROUP_TYPE_NORMAL.equalsIgnoreCase(unitGroup.getUnitGroupType())) {
                if (unitRepository.existsByIsDefaultAndStatusAndUnitGroup(Constants.IS_DEFAULT_TRUE, Constants.STATUS_TRUE, unitGroup)) {
                    throw CustomExceptions.badRequest(MessageConstants.DEFAULT_UNIT_EXIST);
                }
            }
        }

        unit.setName(request.getUnitName());
        unit.setConversionFactor(request.getConversionFactor());
        unit.setIsDefault(request.getIsDefault());
        unit.setUnitGroup(unitGroup);

        unitRepository.save(unit);

        return unitConverter.fromUnitToUnitResponse(unit);
    }


    @Override
    public UnitResponse deleteUnitById(UUID unitId) {
        Unit unit = unitRepository.findByIdAndStatus(unitId, Constants.STATUS_TRUE);
        if (unit == null) {
            throw CustomExceptions.notFound(MessageConstants.NO_UNIT_FOUND + " id: " + unitId);
        }

        unit.setStatus(Constants.STATUS_FALSE);
        unitRepository.save(unit);

        return unitConverter.fromUnitToUnitResponse(unit);
    }

}
