package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.ExportFactorRequest;
import com.example.cabonerfbe.response.ImportFactorResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The interface Excel service.
 *
 * @author SonPHH.
 */
public interface ExcelService {
    /**
     * Read excel method.
     *
     * @param file the file
     * @param name the name
     * @return the import factor response
     * @throws IOException the io exception
     */
    ImportFactorResponse readExcel(MultipartFile file, String name) throws IOException;

    /**
     * Download error log method.
     *
     * @param fileName the file name
     * @return the response entity
     */
    ResponseEntity<Resource> downloadErrorLog(String fileName);

    /**
     * Export factor method.
     *
     * @param request the request
     * @return the response entity
     */
    ResponseEntity<Resource> exportFactor(ExportFactorRequest request);
}
