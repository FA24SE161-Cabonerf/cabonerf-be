package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
import com.example.cabonerfbe.request.UpdateExchangeRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ExchangesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The class Exchange controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.EXCHANGE)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ExchangeController {

    @Autowired
    private ExchangesService exchangesService;

    /**
     * Create elementary exchange method.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping(API_PARAMS.ELEMENTARY_EXCHANGE)
    public ResponseEntity<ResponseObject> createElementaryExchange(@Valid @RequestBody CreateElementaryRequest request) {
        log.info("Start createElementaryExchange. Request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_ELEMENTARY_EXCHANGE_SUCCESS, exchangesService.createElementaryExchanges(request)
        ));
    }

    /**
     * Create product exchange method.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping(API_PARAMS.PRODUCT_EXCHANGE)
    public ResponseEntity<ResponseObject> createProductExchange(@Valid @RequestBody CreateProductRequest request) {
        log.info("Start createProductExchange. Request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_PRODUCT_EXCHANGE_SUCCESS, exchangesService.createProductExchanges(request)
        ));
    }

    /**
     * Remove elementary exchange method.
     *
     * @param exchangeId the exchange id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.ELEMENTARY_EXCHANGE + API_PARAMS.REMOVE_EXCHANGE)
    public ResponseEntity<ResponseObject> removeElementaryExchange(@PathVariable UUID exchangeId) {
        log.info("Start removeElementaryExchange. id: {}", exchangeId);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.REMOVE_EXCHANGE_SUCCESS, exchangesService.removeElementaryExchange(exchangeId)
        ));
    }

    /**
     * Remove product exchange method.
     *
     * @param exchangeId the exchange id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.PRODUCT_EXCHANGE + API_PARAMS.REMOVE_EXCHANGE)
    public ResponseEntity<ResponseObject> removeProductExchange(@PathVariable UUID exchangeId) {
        log.info("Start removeProductExchange. id: {}", exchangeId);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.REMOVE_EXCHANGE_SUCCESS, exchangesService.removeProductExchange(exchangeId)
        ));
    }

    /**
     * Update elementary exchange method.
     *
     * @param exchangeId the exchange id
     * @param request    the request
     * @return the response entity
     */
    @PatchMapping(API_PARAMS.ELEMENTARY_EXCHANGE + API_PARAMS.UPDATE_EXCHANGE)
    public ResponseEntity<ResponseObject> updateElementaryExchange(@PathVariable UUID exchangeId, @Valid @RequestBody UpdateExchangeRequest request) {
        log.info("Start updateElementaryExchange. exchangeId: {}, request: {}", exchangeId, request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_EXCHANGE_SUCCESS, exchangesService.updateElementaryExchange(exchangeId, request)
        ));
    }

    /**
     * Update product exchange method.
     *
     * @param exchangeId the exchange id
     * @param request    the request
     * @return the response entity
     */
    @PatchMapping(API_PARAMS.PRODUCT_EXCHANGE + API_PARAMS.UPDATE_EXCHANGE)
    public ResponseEntity<ResponseObject> updateProductExchange(@PathVariable UUID exchangeId, @Valid @RequestBody UpdateExchangeRequest request) {
        log.info("Start updateProductExchange. exchangeId: {}, request: {}", exchangeId, request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_EXCHANGE_SUCCESS, exchangesService.updateProductExchange(exchangeId, request)
        ));
    }

}
