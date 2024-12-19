package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.CalculateProjectRequest;
import com.example.cabonerfbe.request.CompareProjectsRequest;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.request.ExportProjectRequest;
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


/**
 * The class Project controller.
 *
 * @author SonPHH.
 */
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

    /**
     * Gets project list by method id.
     *
     * @param userId         the user id
     * @param pageCurrent    the page current
     * @param pageSize       the page size
     * @param methodId       the method id
     * @param organizationId the organization id
     * @return the project list by method id
     */
    @GetMapping()
    public ResponseEntity<ResponseObject> getProjectListByMethodId(
            @RequestHeader("x-user-id") UUID userId,
            @RequestParam(defaultValue = "1") int pageCurrent,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) UUID methodId,
            @RequestParam(required = true) UUID organizationId
    ) {
        log.info("Start getAllProject");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_PROJECT_LIST_SUCCESS, projectService.getAllProject(pageCurrent, pageSize, userId, methodId, organizationId)
                ));
    }

    /**
     * Create project method.
     *
     * @param userId  the user id
     * @param request the request
     * @return the response entity
     */
    @PostMapping()
    public ResponseEntity<ResponseObject> createProject(@RequestHeader("x-user-id") UUID userId, @Valid @RequestBody CreateProjectRequest request) {
        log.info("Start createProject. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Create project success", projectService.createProject(userId, request)
                ));
    }

    /**
     * Gets project by id.
     *
     * @param userId    the user id
     * @param projectId the project id
     * @return the project by id
     */
    @GetMapping(API_PARAMS.GET_PROJECT_BY_ID)
    public ResponseEntity<ResponseObject> getProjectById(@RequestHeader("x-user-id") UUID userId, @PathVariable UUID projectId) {
        log.info("Start getProjectById, id: {}", projectId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_PROJECT_BY_ID_SUCCESS, projectService.getById(projectId, userId)
                ));
    }

    /**
     * Update method.
     *
     * @param projectId the project id
     * @param request   the request
     * @param userId    the user id
     * @return the response entity
     */
    @PutMapping(API_PARAMS.UPDATE_DETAIL_PROJECT_BY_ID)
    public ResponseEntity<ResponseObject> update(@PathVariable UUID projectId, @Valid @RequestBody UpdateProjectDetailRequest request, @RequestHeader("x-user-id") UUID userId) {
        log.info("Start updateDetailProject. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Update project details success", projectService.updateDetail(projectId, request, userId)
                ));
    }

    /**
     * Delete method.
     *
     * @param userId    the user id
     * @param projectId the project id
     * @return the response entity
     */
    @DeleteMapping(API_PARAMS.DELETE_PROJECT)
    public ResponseEntity<ResponseObject> delete(@RequestHeader("x-user-id") UUID userId, @PathVariable UUID projectId) {
        log.info("Start deleteProject with projectId: {}", projectId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Delete project success", projectService.deleteProject(userId, projectId)
                ));
    }

    /**
     * Change project method method.
     *
     * @param projectId the project id
     * @param methodId  the method id
     * @return the response entity
     */
    @PatchMapping(API_PARAMS.CHANGE_PROJECT_METHOD)
    public ResponseEntity<ResponseObject> changeProjectMethod(@PathVariable UUID projectId, @PathVariable UUID methodId) {
        log.info("Start changeProjectMethod. projectId: {}, method id: {}", projectId, methodId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CHANGE_PROJECT_METHOD_SUCCESS, projectService.changeProjectMethod(projectId, methodId))
        );
    }

    /**
     * Calculation method.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping(API_PARAMS.CALCULATION_PROJECT)
    public ResponseEntity<ResponseObject> calculation(@RequestBody CalculateProjectRequest request) {
        log.info("Start calculationProject. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Calculation project success", projectService.calculateProject(request))
        );
    }

    /**
     * Export method.
     *
     * @param projectId the project id
     * @return the response entity
     */
    @PostMapping(API_PARAMS.EXPORT_PROJECT)
    public ResponseEntity<Resource> export(@RequestBody ExportProjectRequest request) {
        log.info("Start exportProject. Id: {}", request.getProjectId());
        return projectService.exportProject(request);
    }

    /**
     * Intensity method.
     *
     * @param projectId the project id
     * @return the response entity
     */
    @GetMapping(API_PARAMS.INTENSITY_PROJECT)
    public ResponseEntity<ResponseObject> intensity(@PathVariable("projectId") UUID projectId) {
        log.info("Start intensity. id: {}", projectId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get intensity success", projectService.getIntensity(projectId))
        );
    }

    /**
     * Count all project method.
     *
     * @return the response entity
     */
    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.COUNT_PROJECT)
    public ResponseEntity<ResponseObject> countAllProject() {
        log.info("Start countAllProject");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Count all project success", projectService.countAllProject())
        );
    }

    /**
     * Gets sum impact.
     *
     * @return the sum impact
     */
    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.SUM_IMPACT)
    public ResponseEntity<ResponseObject> getSumImpact() {
        log.info("Start getImpactSum");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get sum impact success", projectService.countImpactInDashboard())
        );
    }

    /**
     * Favorite method.
     *
     * @param projectId the project id
     * @return the response entity
     */
    @PutMapping(API_PARAMS.SET_FAVORITE_PROJECT)
    public ResponseEntity<ResponseObject> favorite(@PathVariable UUID projectId) {
        log.info("Start updateFavorite. projectId: {}", projectId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Update favorite success", projectService.updateFavorite(projectId))
        );
    }

    @PostMapping(API_PARAMS.COMPARISONS)
    public ResponseEntity<ResponseObject> compareProjects(@RequestBody CompareProjectsRequest request) {
        log.info("Start compareProjects. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.COMPARE_PROJECTS_SUCCESS, projectService.compareProjects(request))
        );
    }
}
