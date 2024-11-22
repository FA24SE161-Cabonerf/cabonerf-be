package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.WorkspaceService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.WORKSPACE)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllByUser(@RequestHeader("x-user-id") UUID userId){
        log.info("start getAllByUser. userId: {}",userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Get all workspace success", workspaceService.getByUser(userId))
        );
    }
}
