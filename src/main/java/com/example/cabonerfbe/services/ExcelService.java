package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.ImportEmissionSubstanceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    ImportEmissionSubstanceResponse readExcel(MultipartFile file, String name);
}
