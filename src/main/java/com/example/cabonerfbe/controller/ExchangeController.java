package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateElementaryRequest;
import com.example.cabonerfbe.request.CreateProductRequest;
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

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.EXCHANGE)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ExchangeController {

    @Autowired
    private ExchangesService exchangesService;

    @PostMapping(API_PARAMS.CREATE_ELEMENTARY_EXCHANGE)
    public ResponseEntity<ResponseObject> createElementaryExchange(@Valid @RequestBody CreateElementaryRequest request) {
        log.info("Start createElementaryExchange. Request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_ELEMENTARY_EXCHANGE_SUCCESS, exchangesService.createElementaryExchanges(request)
        ));
    }

    @PostMapping(API_PARAMS.CREATE_PRODUCT_EXCHANGE)
    public ResponseEntity<ResponseObject> createProductExchange(@Valid @RequestBody CreateProductRequest request) {
        log.info("Start createProductExchange. Request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CREATE_PRODUCT_EXCHANGE_SUCCESS, exchangesService.createProductExchanges(request)
        ));
    }

    @DeleteMapping(API_PARAMS.REMOVE_EXCHANGE)
    public ResponseEntity<ResponseObject> removeExchange(@PathVariable UUID exchangeId) {
        log.info("Start removeExchange. id: {}", exchangeId);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.REMOVE_EXCHANGE_SUCCESS, exchangesService.removeExchange(exchangeId)
        ));
    }


}
