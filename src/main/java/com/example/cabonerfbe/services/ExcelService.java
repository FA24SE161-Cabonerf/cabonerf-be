package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.request.ExportFactorRequest;
import com.example.cabonerfbe.response.ImportEmissionSubstanceResponse;
import com.example.cabonerfbe.response.ImportFactorResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ExcelService {
    ImportFactorResponse readExcel(MultipartFile file, String name) throws IOException;

    ResponseEntity<Resource> downloadErrorLog(String fileName);

    ResponseEntity<Resource> downloadTemplate();
    ResponseEntity<Resource> exportFactor(ExportFactorRequest request);
}
