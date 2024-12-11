package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreatePerspectiveRequest;
import com.example.cabonerfbe.request.UpdatePerspectiveRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.PerspectiveService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The class Perspective controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.PERSPECTIVE)
@RestController
@NoArgsConstructor
@AllArgsConstructor
@CrossOrigin("*")
@Slf4j
public class PerspectiveController {
    /**
     * The Perspective service.
     */
    @Autowired
    PerspectiveService perspectiveService;

    /**
     * Gets all perspective.
     *
     * @return the all perspective
     */
    @GetMapping(API_PARAMS.GET_ALL_PERSPECTIVE)
    public ResponseEntity<ResponseObject> getAllPerspective() {
        log.info("Start getAllPerspective");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_PERSPECTIVE_SUCCESS, perspectiveService.getAllPerspective())
        );
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    @GetMapping(API_PARAMS.GET_BY_ID_PERSPECTIVE)
    public ResponseEntity<ResponseObject> getById(@PathVariable UUID id) {
        log.info("Start getPerspectiveById. Id: {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_PERSPECTIVE_SUCCESS, perspectiveService.getPerspectiveById(id))
        );
    }

    /**
     * Update by id method.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @PutMapping(API_PARAMS.GET_BY_ID_PERSPECTIVE)
    public ResponseEntity<ResponseObject> updateById(@PathVariable UUID id, @RequestBody UpdatePerspectiveRequest request) {
        log.info("Start updateById. Id: {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_PERSPECTIVE_SUCCESS, perspectiveService.updatePerspective(request, id))
        );
    }

    /**
     * Create perspective method.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping()
    public ResponseEntity<ResponseObject> createPerspective(@Valid @RequestBody CreatePerspectiveRequest request) {
        log.info("Start createPerspective");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create Perspective success", perspectiveService.createPerspective(request))
        );
    }

    /**
     * Delete perspective method.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.GET_BY_ID_PERSPECTIVE)
    public ResponseEntity<ResponseObject> deletePerspective(@PathVariable UUID id) {
        log.info("Start deletePerspective. Id: {}", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create Perspective success", perspectiveService.deletePerspective(id))
        );
    }
}
