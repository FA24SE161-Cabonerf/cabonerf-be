package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.request.CreateConnectorRequest;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.ConnectorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.CONNECTOR)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class ConnectorController {
    @Autowired
    private ConnectorService service;

    @PostMapping()
    public ResponseEntity<ResponseObject> createConnector(@Valid @RequestBody CreateConnectorRequest request){
        return ResponseEntity.ok().body(new ResponseObject(
                Constants.RESPONSE_STATUS_SUCCESS,"Create connector success",service.createConnector(request)
        ));
    }
}
