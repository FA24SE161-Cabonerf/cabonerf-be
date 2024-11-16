package com.example.cabonerfbe.services;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String updateFile(String key, byte[] fileData);
    byte[] downloadFile(String key);
}
