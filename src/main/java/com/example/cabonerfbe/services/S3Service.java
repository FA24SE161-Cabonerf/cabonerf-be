package com.example.cabonerfbe.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * The interface S 3 service.
 *
 * @author SonPHH.
 */
public interface S3Service {
    /**
     * Update file method.
     *
     * @param key      the key
     * @param fileData the file data
     * @return the string
     */
    String updateFile(String key, byte[] fileData);

    /**
     * Download file method.
     *
     * @param publicUrl the public url
     * @return the byte [ ]
     */
    byte[] downloadFile(String publicUrl);

    /**
     * Delete file method.
     *
     * @param publicUrl the public url
     */
    void deleteFile(String publicUrl);

    /**
     * Upload image method.
     *
     * @param file the file
     * @return the string
     */
    String uploadImage(MultipartFile file);

    /**
     * Upload contract method.
     *
     * @param file the file
     * @return the string
     */
    String uploadContract(MultipartFile file);

    /**
     * Upload error method.
     *
     * @param file the file
     * @return the string
     */
    String uploadError(MultipartFile file);
}
