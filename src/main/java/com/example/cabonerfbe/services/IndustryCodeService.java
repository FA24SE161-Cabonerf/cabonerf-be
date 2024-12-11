package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.IndustryCodeDto;
import com.example.cabonerfbe.request.IndustryCodeRequest;
import com.example.cabonerfbe.response.GetAllIndustryCodeResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Industry code service.
 *
 * @author SonPHH.
 */
public interface IndustryCodeService {
    /**
     * Gets all.
     *
     * @param pageCurrent the page current
     * @param pageSize    the page size
     * @param keyword     the keyword
     * @return the all
     */
    GetAllIndustryCodeResponse getAll(int pageCurrent, int pageSize, String keyword);

    /**
     * Gets all to create organization.
     *
     * @param keyword the keyword
     * @return the all to create organization
     */
    List<IndustryCodeDto> getAllToCreateOrganization(String keyword);

    /**
     * Create method.
     *
     * @param request the request
     * @return the industry code dto
     */
    IndustryCodeDto create(IndustryCodeRequest request);

    /**
     * Update method.
     *
     * @param id      the id
     * @param request the request
     * @return the industry code dto
     */
    IndustryCodeDto update(UUID id, IndustryCodeRequest request);

    /**
     * Delete method.
     *
     * @param id the id
     * @return the industry code dto
     */
    IndustryCodeDto delete(UUID id);

    /**
     * Gets in organization.
     *
     * @param organizationId the organization id
     * @return the in organization
     */
    List<IndustryCodeDto> getInOrganization(UUID organizationId);
}
