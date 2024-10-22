package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ConnectorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.CONNECTOR)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ConnectorController {
    @Autowired
    private ConnectorService service;

    @PostMapping()
    public ResponseEntity<ResponseObject> createConnector(@Valid @RequestBody CreateConnectorRequest request){
        log.info("Start createConnector. request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,"Create connector success",service.createConnector(request)
        ));
    }
}
