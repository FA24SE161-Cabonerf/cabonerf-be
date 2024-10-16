package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.PROJECT)
@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping(API_PARAMS.GET_PROJECT_LIST_BY_METHOD_ID)
    public ResponseEntity<ResponseObject> getProjectListByMethodId(@PathVariable long id) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_PROJECT_LIST_SUCCESS, projectService.getProjectListByMethodId(id)
                ));
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> createProject(@Valid @RequestBody CreateProjectRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create project success", projectService.createProject(request)
                ));
    }
}
