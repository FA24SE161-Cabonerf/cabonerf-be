package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UnitGroupConverter;
import com.example.cabonerfbe.dto.UnitGroupDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.UnitGroup;
import com.example.cabonerfbe.repositories.UnitGroupRepository;
import com.example.cabonerfbe.services.UnitGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitGroupServiceImpl implements UnitGroupService {

    @Autowired
    private UnitGroupRepository repository;
    @Autowired
    private UnitGroupConverter converter;

    @Override
    public List<UnitGroupDto> getAllUnitGroup() {
        return converter.INSTANCE.fromListUnitGroupToUnitGroupDto(repository.findAll());
    }

    @Override
    public UnitGroupDto getUnitGroupById(long id) {
        if(repository.findById(id).isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, MessageConstants.GET_UNIT_GROUP_BY_ID_NOT_FOUND);
        }
        return converter.INSTANCE.fromUnitGroupToUnitGroupDto(repository.findById(id).get());
    }
}
