package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ProcessService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.PROCESS)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ProcessController {
    @Autowired
    private ProcessService processService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllProcess(
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "0") long projectId) {
        log.info("Start getAllProcess. CurrentPage: {}, pageSize: {}, ProjectId: {}", currentPage, pageSize, projectId);
        return ResponseEntity.ok().body(new ResponseObject(
                        Constants.RESPONSE_STATUS_SUCCESS, "Get all process success", processService.getAllProcesses(currentPage, pageSize, projectId)
                )
        );
    }

    @GetMapping(API_PARAMS.PROCESS_BY_ID)
    public ResponseEntity<ResponseObject> getProcessById(@PathVariable long id) {
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


}
