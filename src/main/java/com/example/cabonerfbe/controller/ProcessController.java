package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ProcessService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.PROCESS)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ProcessController {
    @Autowired
    private ProcessService processService;

//    @GetMapping()
//    public ResponseEntity<ResponseObject> getAllProcess(@Valid @RequestBody GetAllProcessRequest request) {
//        log.info("Start getAllProcess. request: {}", request);
//        return ResponseEntity.ok().body(new ResponseObject(
//                        Constants.RESPONSE_STATUS_SUCCESS, "Get all process success", processService.getAllProcesses(request)
//                )
//        );
//    }

    @GetMapping(API_PARAMS.PROCESS_BY_ID)
    public ResponseEntity<ResponseObject> getProcessById(@PathVariable UUID id) {
        log.info("Start getProcessById. id: {}", id);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Get process by id success", processService.getProcessById(id)
                )
        );
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> createProcess(@Valid @RequestBody CreateProcessRequest request) {
        log.info("Start createProcess. request: {}", request);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Create process success", processService.createProcess(request)
                )
        );
    }

    @PutMapping(API_PARAMS.PROCESS_BY_ID)
    public ResponseEntity<ResponseObject> updateProcess(@Valid @RequestBody UpdateProcessRequest request, @PathVariable UUID id) {
        log.info("Start updateProcess. id: {}, request: {}", id, request);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Create process success", processService.updateProcess(id, request)
                )
        );
    }

    @DeleteMapping(API_PARAMS.PROCESS_BY_ID)
    public ResponseEntity<ResponseObject> deleteProcess(@PathVariable UUID id) {
        log.info("Start updateProcess. id: {}", id);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Create process success", processService.deleteProcess(id)
                )
        );
    }


}
