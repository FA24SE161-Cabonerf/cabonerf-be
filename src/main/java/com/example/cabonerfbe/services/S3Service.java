package com.example.cabonerfbe.services;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String updateFile(String key, byte[] fileData);
    byte[] downloadFile(String publicUrl);
    void deleteFile(String publicUrl);
    String uploadImage(MultipartFile file);
    String uploadContract(MultipartFile file);
    String uploadError(MultipartFile file);
}
