package com.example.caboneftbe.services;

import com.example.caboneftbe.response.ImportEmissionSubstanceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    ImportEmissionSubstanceResponse readExcel(MultipartFile file);
}
