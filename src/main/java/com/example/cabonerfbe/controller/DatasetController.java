package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.DatasetService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The class Dataset controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.DATASET)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class DatasetController {
    @Autowired
    private DatasetService datasetService;

    /**
     * Gets all datasets.
     *
     * @param userId    the user id
     * @param projectId the project id
     * @return the all datasets
     */
    @GetMapping()
    public ResponseEntity<ResponseObject> getAllDatasets(@RequestHeader(value = Constants.USER_ID_HEADER) UUID userId, @RequestParam UUID projectId) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.GET_ALL_DATASET_SUCCESS, datasetService.getAllDataset(userId, projectId))
        );
    }

    /**
     * Gets all by admin.
     *
     * @param pageCurrent the page current
     * @param pageSize    the page size
     * @param keyword     the keyword
     * @return the all by admin
     */
    @GetMapping(API_PARAMS.ADMIN)
    public ResponseEntity<ResponseObject> getAllByAdmin(@RequestParam(defaultValue = "1") int pageCurrent,
                                                        @RequestParam(defaultValue = "5") int pageSize,
                                                        @RequestParam(required = false) String keyword) {
        log.info("Start getAllDataByAdmin");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get all data set by admin success", datasetService.get(pageCurrent, pageSize, keyword))
        );
    }

}
