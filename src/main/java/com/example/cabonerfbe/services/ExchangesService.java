package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.request.UpdateExchangeRequest;
import com.example.cabonerfbe.response.ImpactExchangeResponse;
import com.example.cabonerfbe.response.SearchElementaryResponse;
import com.example.cabonerfbe.response.UpdateProductExchangeResponse;

import java.util.List;
import java.util.UUID;

/**
 * The interface Exchanges service.
 *
 * @author SonPHH.
 */
public interface ExchangesService {

    /**
     * Create elementary exchanges method.
     *
     * @param request the request
     * @return the list
     */
    List<ExchangesDto> createElementaryExchanges(CreateElementaryRequest request);

    /**
     * Create product exchanges method.
     *
     * @param request the request
     * @return the list
     */
    List<ExchangesDto> createProductExchanges(CreateProductRequest request);

    /**
     * Search method.
     *
     * @param pageCurrent           the page current
     * @param pageSize              the page size
     * @param keyWord               the key word
     * @param methodId              the method id
     * @param emissionCompartmentId the emission compartment id
     * @param impactCategoryId      the impact category id
     * @param input                 the input
     * @return the search elementary response
     */
    SearchElementaryResponse search(int pageCurrent, int pageSize, String keyWord, UUID methodId, UUID emissionCompartmentId, UUID impactCategoryId, boolean input);

    /**
     * Gets all admin.
     *
     * @param keyword the keyword
     * @return the all admin
     */
    List<EmissionSubstanceDto> getAllAdmin(String keyword);

    /**
     * Remove elementary exchange method.
     *
     * @param exchangeId the exchange id
     * @return the impact exchange response
     */
    ImpactExchangeResponse removeElementaryExchange(UUID exchangeId);

    /**
     * Remove product exchange method.
     *
     * @param exchangeId the exchange id
     * @return the list
     */
    List<ExchangesDto> removeProductExchange(UUID exchangeId);

    /**
     * Update elementary exchange method.
     *
     * @param exchangeId the exchange id
     * @param request    the request
     * @return the impact exchange response
     */
    ImpactExchangeResponse updateElementaryExchange(UUID exchangeId, UpdateExchangeRequest request);

    /**
     * Update product exchange method.
     *
     * @param exchangeId the exchange id
     * @param request    the request
     * @return the list
     */
    List<UpdateProductExchangeResponse> updateProductExchange(UUID exchangeId, UpdateExchangeRequest request);

}
