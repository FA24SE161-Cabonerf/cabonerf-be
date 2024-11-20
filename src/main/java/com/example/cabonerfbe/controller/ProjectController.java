package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.request.UpdateProjectDetailRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ProcessImpactValueService;
import com.example.cabonerfbe.services.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
    @Autowired
    private ProcessImpactValueService service;

    @GetMapping()
    public ResponseEntity<ResponseObject> getProjectListByMethodId(
            @RequestHeader("x-user-id") UUID userId,
            @RequestParam(defaultValue = "1") int pageCurrent,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false) UUID methodId) {
        log.info("Start getAllProject");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_PROJECT_LIST_SUCCESS, projectService.getAllProject(pageCurrent,pageSize,userId, methodId)
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
    public ResponseEntity<ResponseObject> getProjectById(@PathVariable UUID projectId, @PathVariable UUID workspaceId) {
        log.info("Start getProjectById, id: " + projectId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_PROJECT_BY_ID_SUCCESS, projectService.getById(projectId, workspaceId)
                ));
    }

    @PutMapping(API_PARAMS.UPDATE_DETAIL_PROJECT_BY_ID)
    public ResponseEntity<ResponseObject> update(@PathVariable UUID projectId, @Valid @RequestBody UpdateProjectDetailRequest request){
        log.info("Start updateDetailProject. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Update project details success", projectService.updateDetail(projectId,request)
                ));
    }

    @DeleteMapping(API_PARAMS.DELETE_PROJECT)
    public ResponseEntity<ResponseObject> delete(@PathVariable UUID projectId){
        log.info("Start deleteProject with projectId: {}", projectId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Delete project success", projectService.deleteProject(projectId)
                ));
    }

    @PatchMapping(API_PARAMS.CHANGE_PROJECT_METHOD)
    public ResponseEntity<ResponseObject> changeProjectMethod(@PathVariable UUID projectId, @PathVariable UUID methodId){
        log.info("Start changeProjectMethod. projectId: {}, method id: {}", projectId, methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CHANGE_PROJECT_METHOD_SUCCESS, projectService.changeProjectMethod(projectId, methodId))
        );
    }

    @GetMapping(API_PARAMS.CALCULATION_PROJECT)
    public ResponseEntity<ResponseObject> calculation(@PathVariable("projectId") UUID projectId){
        log.info("Start calculationProject. Id: {}", projectId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Calculation project success",  projectService.getProjectById(projectId))
        );
    }

    @GetMapping(API_PARAMS.EXPORT_PROJECT)
    public ResponseEntity<Resource> export(@PathVariable("projectId") UUID projectId){
        log.info("Start exportProject. Id: {}", projectId);
        return projectService.exportProject(projectId);
    }
}
