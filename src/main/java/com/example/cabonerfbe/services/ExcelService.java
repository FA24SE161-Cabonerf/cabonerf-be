package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.ImportEmissionSubstanceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelService {
    void  readExcel(MultipartFile file, String name) throws IOException;
}
