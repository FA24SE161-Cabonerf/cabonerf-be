package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.request.UpdateProjectDetailRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.PROJECT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getProjectListByMethodId(
            @RequestHeader("x-user-id") UUID userId,
            @RequestParam(defaultValue = "1") int pageCurrent,
            @RequestParam(defaultValue = "5") int pageSize) {
        log.info("Start getAllProject");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_PROJECT_LIST_SUCCESS, projectService.getAllProject(pageCurrent,pageSize,userId)
                ));
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> createProject(@RequestHeader("x-user-id") UUID userId, @Valid @RequestBody CreateProjectRequest request){
        log.info("Start createProject. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create project success", projectService.createProject(userId, request)
                ));
    }

    @GetMapping(API_PARAMS.GET_PROJECT_BY_ID)
    public ResponseEntity<ResponseObject> getProjectById(@PathVariable UUID id) {
        log.info("Start getProjectById, id: " + id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_PROJECT_BY_ID_SUCCESS, projectService.getById(id)
                ));
    }

    @PutMapping(API_PARAMS.UPDATE_DETAIL_PROJECT_BY_ID)
    public ResponseEntity<ResponseObject> update(@PathVariable UUID id, @Valid @RequestBody UpdateProjectDetailRequest request){
        log.info("Start updateDetailProject. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Update project details success", projectService.updateDetail(id,request)
                ));
    }

    @DeleteMapping(API_PARAMS.DELETE_PROJECT)
    public ResponseEntity<ResponseObject> delete(@PathVariable UUID id){
        log.info("Start deleteProject with id: ", id);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Delete project success", projectService.deleteProject(id)
                ));
    }
}
