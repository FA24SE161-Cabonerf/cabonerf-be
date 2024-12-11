package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * The class S 3 service.
 *
 * @author SonPHH.
 */
@Service
public class S3ServiceImpl implements S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private S3Client client;
    @Autowired
    private S3Client s3Client;

    @Override
    public String updateFile(String key, byte[] file) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            client.putObject(request, RequestBody.fromBytes(file));
        } catch (Exception ignored) {

        }
        return key;
    }

    @Override
    public byte[] downloadFile(String publicUrl) {
        try {
            // Trích xuất key từ Public URL
            String key = extractKeyFromPublicUrl(publicUrl);

            // Tạo yêu cầu tải file
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Tải file dưới dạng byte array
            ResponseBytes<GetObjectResponse> objectBytes = client.getObjectAsBytes(getObjectRequest);
            return objectBytes.asByteArray();
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to download from S3.", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid URL.", e);
        }
    }

    @Override
    public void deleteFile(String publicUrl) {
        try {
            // Trích xuất key từ Public URL
            String key = extractKeyFromPublicUrl(publicUrl);

            // Tạo yêu cầu xóa file
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Thực thi lệnh xóa file
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to delete from S3.", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid URL.", e);
        }
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());

            // Set content type based on file
            String contentType = file.getContentType();

            // Create PutObject request
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            // Upload file
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Generate public URL
            return generatePublicUrl(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image.", e);
        }
    }

    @Override
    public String uploadContract(MultipartFile file) {
        try {
            // Generate unique filename
            String fileName = generatePDFFileName(file.getOriginalFilename());

            // Set content type based on file
            String contentType = file.getContentType();

            // Create PutObject request
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            // Upload file
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Generate public URL
            return generatePublicUrl(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload contract.", e);
        }
    }

    @Override
    public String uploadError(MultipartFile file) {
        try {
            // Generate unique filename
            String fileName = generateErrorFileName(file.getOriginalFilename());

            // Set content type based on file
            String contentType = file.getContentType();

            // Create PutObject request
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            // Upload file
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Generate public URL
            return generatePublicUrl(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload contract.", e);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        String fileExtension = getFileExtension(originalFileName);
        return "image/" + timestamp + "-" + randomString + fileExtension;
    }

    private String generatePDFFileName(String originalFileName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        String fileExtension = getFileExtension(originalFileName);
        return "contract/" + timestamp + "-" + randomString + fileExtension;
    }

    private String generateErrorFileName(String originalFileName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        String fileExtension = getFileExtension(originalFileName);
        return "error/" + timestamp + "-" + randomString + fileExtension;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? "." + fileName.substring(dotIndex + 1) : "";
    }

    private String generatePublicUrl(String fileName) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
    }

    private String extractKeyFromPublicUrl(String publicUrl) {
        try {
            // Phân tích URL để lấy đường dẫn (path)
            URI uri = new URI(publicUrl);
            String path = uri.getPath();
            // Loại bỏ ký tự '/' ở đầu nếu có
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot extract key from Public URL: " + publicUrl, e);
        }
    }
}
