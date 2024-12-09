package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.ExportFactorRequest;
import com.example.cabonerfbe.response.ImportFactorResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelService {
    ImportFactorResponse readExcel(MultipartFile file, String name) throws IOException;

    ResponseEntity<Resource> downloadErrorLog(String fileName);

    ResponseEntity<Resource> exportFactor(ExportFactorRequest request);
}
