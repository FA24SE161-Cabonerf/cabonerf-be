package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UnitConverter;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.dto.UnitDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Exchanges;
import com.example.cabonerfbe.models.Unit;
import com.example.cabonerfbe.repositories.UnitRepository;
import com.example.cabonerfbe.response.CreateProcessResponse;
import com.example.cabonerfbe.services.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private UnitConverter converter;

    @Override
    public PageList<UnitDto> getAllUnit(int currentPage, int pageSize, long unitGroupId) {
        if (unitGroupId == 0) {
            if (currentPage < 1 || pageSize < 1) {
                List<UnitDto> list = getAllUnitDefault();
                PageList<UnitDto> pageList = new PageList<>();
                pageList.setCurrentPage(1);
                pageList.setTotalPage(1);
                pageList.setListResult(list);
                return pageList;
            }

            List<UnitDto> pagedList = getAllUnit(currentPage, pageSize);
            int totalRecords = pagedList.size();

            int totalPage = (int) Math.ceil((double) unitRepository.findAll().size() / pageSize);

            PageList<UnitDto> pageList = new PageList<>();
            pageList.setCurrentPage(currentPage);
            pageList.setTotalPage(totalPage);
            pageList.setListResult(pagedList);

            return pageList;
        }
        if (currentPage < 1 || pageSize < 1) {
            List<UnitDto> list = getAllUnitDefaultWithUnitGroup(unitGroupId);
            PageList<UnitDto> pageList = new PageList<>();
            pageList.setCurrentPage(1);
            pageList.setTotalPage(1);
            pageList.setListResult(list);
            return pageList;
        }

        List<UnitDto> pagedList = getAllUnitWithUnitGroup(currentPage, pageSize,unitGroupId);
        int totalRecords = pagedList.size();

        int totalPage = (int) Math.ceil((double) unitRepository.findAllByUnitGroupId(unitGroupId).size() / pageSize);

        PageList<UnitDto> pageList = new PageList<>();
        pageList.setCurrentPage(currentPage);
        pageList.setTotalPage(totalPage);
        pageList.setListResult(pagedList);

        return pageList;
    }

    @Override
    public UnitDto getById(long id) {
        if(unitRepository.findById(id).isEmpty()){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Unit not exist");
        }
        return converter.INSTANCE.fromUnitToUnitDto(unitRepository.findById(id).get());
    }

    private List<UnitDto> getAllUnitDefault() {
        List<Unit> units = unitRepository.findAll();
        return buildUnitResponse(units);
    }

    private List<UnitDto> getAllUnit(int currentPage, int pageSize) {
        Page<Unit> units = unitRepository.findAll(PageRequest.of(currentPage - 1, pageSize));
        return buildUnitResponse(units.getContent());
    }

    private List<UnitDto> getAllUnitDefaultWithUnitGroup(long unitGroupId) {
        List<Unit> units = unitRepository.findAllByUnitGroupId(unitGroupId);
        return buildUnitResponse(units);
    }

    private List<UnitDto> getAllUnitWithUnitGroup(int currentPage, int pageSize, long unitGroupId) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Unit> units = unitRepository.findAllByUnitGroupIdWithPage(unitGroupId,pageable);
        return buildUnitResponse(units.getContent());
    }

    private List<UnitDto> buildUnitResponse(List<Unit> units) {
        return converter.INSTANCE.fromListUnitToUnitDto(units);
    }
}
