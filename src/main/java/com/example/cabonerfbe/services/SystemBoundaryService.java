package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.SystemBoundaryResponse;

import java.util.List;

/**
 * The interface System boundary service.
 *
 * @author SonPHH.
 */
public interface SystemBoundaryService {
    /**
     * Gets all system boundary.
     *
     * @return the all system boundary
     */
    List<SystemBoundaryResponse> getAllSystemBoundary();
}
