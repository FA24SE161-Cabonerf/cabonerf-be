package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.request.CreateElementaryRequest;
import com.example.caboneftbe.request.CreateProductRequest;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.ExchangesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.EXCHANGE)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class ExchangeController {

    @Autowired
    private ExchangesService exchangesService;

    @PostMapping(API_PARAMS.CREATE_ELEMENTARY_EXCHANGE)
    public ResponseEntity<ResponseObject> createElementaryExchange(@Valid @RequestBody CreateElementaryRequest request){
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,"Create elementary exchanges success",exchangesService.createElementaryExchanges(request)
        ));
    }

    @PostMapping(API_PARAMS.CREATE_PRODUCT_EXCHANGE)
    public ResponseEntity<ResponseObject> createProductExchange(@Valid @RequestBody CreateProductRequest request){
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,"Create product exchanges success",exchangesService.createProductExchanges(request)
        ));
    }
}