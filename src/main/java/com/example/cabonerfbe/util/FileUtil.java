package com.example.cabonerfbe.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class FileUtil {
    public boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        boolean isMimeTypeImage = isImageFileMime(file);
        boolean isExtensionImage = isImageFileExtension(file);
        boolean isContentValidImage = isValidImageFile(file);

        return isMimeTypeImage && isExtensionImage && isContentValidImage;
    }

    public boolean isImageFileMime(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public boolean isImageFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            return fileName.toLowerCase().matches(".*\\.(png|jpg|jpeg|gif|bmp|webp)$");
        }
        return false;
    }

    public boolean isValidImageFile(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isPdfFile(MultipartFile file) {
        // Cách 1: Kiểm tra theo Content Type
        if (file.getContentType() != null && file.getContentType().equals("application/pdf")) {
            return true;
        }

        // Cách 2: Kiểm tra theo đuôi file
        String filename = file.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
            return true;
        }

        // Cách 3: Kiểm tra Magic Number của PDF file
        try {
            byte[] bytes = file.getBytes();
            if (bytes.length > 4 &&
                    bytes[0] == 0x25 && // %
                    bytes[1] == 0x50 && // P
                    bytes[2] == 0x44 && // D
                    bytes[3] == 0x46 && // F
                    bytes[4] == 0x2D) { // -
                return true;
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }
}
