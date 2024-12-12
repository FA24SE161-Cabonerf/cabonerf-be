package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.DatasetAdminResponse;
import com.example.cabonerfbe.response.DatasetResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Dataset service.
 *
 * @author SonPHH.
 */
public interface DatasetService {

    /**
     * Gets all dataset.
     *
     * @param userId    the user id
     * @param projectId the project id
     * @return the all dataset
     */
    List<DatasetResponse> getAllDataset(UUID userId, UUID projectId);

    /**
     * Get method.
     *
     * @param pageCurrent the page current
     * @param pageSize    the page size
     * @param keyword     the keyword
     * @return the dataset admin response
     */
    DatasetAdminResponse get(int pageCurrent, int pageSize, String keyword);

}
