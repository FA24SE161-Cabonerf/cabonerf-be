package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.enums.MessageConstants;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.ProjectService;
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
}
