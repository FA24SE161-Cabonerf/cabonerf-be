package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.SystemBoundaryConverter;
import com.example.cabonerfbe.repositories.SystemBoundaryRepository;
import com.example.cabonerfbe.response.SystemBoundaryResponse;
import com.example.cabonerfbe.services.SystemBoundaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemBoundaryServiceImpl implements SystemBoundaryService {
    private final SystemBoundaryRepository systemBoundaryRepository;
    private final SystemBoundaryConverter systemBoundaryConverter;

    @Autowired
    public SystemBoundaryServiceImpl(SystemBoundaryRepository systemBoundaryRepository, SystemBoundaryConverter systemBoundaryConverter) {
        this.systemBoundaryRepository = systemBoundaryRepository;
        this.systemBoundaryConverter = systemBoundaryConverter;
    }

    @Override
    public List<SystemBoundaryResponse> getAllSystemBoundary() {
        return systemBoundaryConverter.fromListEntityToListResponse(systemBoundaryRepository.findAllTrue());
    }
}
