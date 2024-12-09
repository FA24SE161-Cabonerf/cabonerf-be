package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.request.LifeCycleStagesRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.LifeCycleStageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.LIFE_STAGE)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class LifeCycleStageController {

    @Autowired
    private LifeCycleStageService lifeCycleStageService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllLifeCycleStage() {
        log.info("Start getAllLifeCycleStage");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get all life cycle stage success", lifeCycleStageService.getAll())
        );
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody LifeCycleStagesRequest request) {
        log.info("Start createLifeCycleStages. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create life cycle stages success", lifeCycleStageService.create(request))
        );
    }

    @PutMapping(API_PARAMS.LIFE_CYCLE_STAGE_UPDATE)
    public ResponseEntity<ResponseObject> update(@PathVariable UUID id, @Valid @RequestBody LifeCycleStagesRequest request) {
        log.info("Start updateLifeCycleStages. Id: {}, request: {}", id, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Update life cycle stages success", lifeCycleStageService.update(id, request))
        );
    }

    @DeleteMapping(API_PARAMS.LIFE_CYCLE_STAGE_DELETE)
    public ResponseEntity<ResponseObject> delete(@PathVariable UUID id) {
        log.info("Start deleteLifeCycleStages. Id: {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Delete life cycle stages success", lifeCycleStageService.delete(id))
        );
    }
}
