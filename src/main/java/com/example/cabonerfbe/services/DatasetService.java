package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.DatasetAdminResponse;
import com.example.cabonerfbe.response.DatasetResponse;

import java.util.List;
import java.util.UUID;

public interface DatasetService {

    List<DatasetResponse> getAllDataset(UUID userId, UUID projectId);

    DatasetAdminResponse get(int pageCurrent, int pageSize, String keyword);

}
