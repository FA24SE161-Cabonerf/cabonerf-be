package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UserConverter;
import com.example.cabonerfbe.dto.UserAdminDto;
import com.example.cabonerfbe.dto.UserProfileDto;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.response.GetAllUserResponse;
import com.example.cabonerfbe.response.GetProfileResponse;
import com.example.cabonerfbe.response.GetUserToInviteResponse;
import com.example.cabonerfbe.response.UpdateAvatarUserResponse;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.services.S3Service;
import com.example.cabonerfbe.services.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true)
@SuperBuilder
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserConverter userConverter = UserConverter.INSTANCE;

    @Autowired
    S3Service s3Service;

    @Override
    public GetProfileResponse getMe(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_FOUND));
        if (!user.isStatus()) {
            throw CustomExceptions.unauthorized(MessageConstants.USER_IS_BANNED);
        }
        UserProfileDto userProfileDtoDto = userConverter.fromUserToUserProfileDto(user);

        return userConverter.fromUserProfileDtoToGetProfileResponse(userProfileDtoDto);
    }

    @Override
    public GetAllUserResponse getAll(int pageCurrent, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);

        Page<Users> users = keyword != null
                ? userRepository.findByEmailAndFullName(keyword, pageable)
                : userRepository.findAll(pageable);

        int totalPage = users.getTotalPages();
        if (pageCurrent > totalPage) {
            return GetAllUserResponse.builder()
                    .pageCurrent(1)
                    .pageSize(0)
                    .totalPage(0)
                    .users(Collections.emptyList())
                    .build();
        }

        return GetAllUserResponse.builder()
                .pageCurrent(pageCurrent)
                .pageSize(pageSize)
                .totalPage(totalPage)
                .users(users.stream().map(userConverter::forAdmin).collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserAdminDto updateUserStatus(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        u.setStatus(!u.isStatus());
        return userConverter.forAdmin(userRepository.save(u));
    }

    @Override
    public UpdateAvatarUserResponse updateAvatarUser(UUID userId, MultipartFile file) {
        Users u = userRepository.findByIdWithStatus(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));
        if (!isImageFile(file)) {
            throw CustomExceptions.badRequest("Invalid image.");
        }

        if (u.getProfilePictureUrl() != null) {
            s3Service.deleteFile(u.getProfilePictureUrl());
        }
        try {
            u.setProfilePictureUrl(s3Service.uploadImage(file));
        } catch (Exception ignored) {
        }


        return userConverter.forUpdateAvatar(userRepository.save(u));
    }

    @Override
    public GetUserToInviteResponse getToInvite(int pageCurrent, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);

        Page<Users> data = keyword == null
                ? userRepository.findToInvite(pageable)
                : userRepository.findToInviteByKeyword(keyword, pageable);

        int totalPage = data.getTotalPages();
        if (pageCurrent > totalPage) {
            return GetUserToInviteResponse.builder()
                    .totalPage(0)
                    .pageSize(0)
                    .pageCurrent(1)
                    .users(Collections.emptyList())
                    .build();
        }

        return GetUserToInviteResponse.builder()
                .totalPage(totalPage)
                .pageSize(pageSize)
                .pageCurrent(pageCurrent)
                .users(data.stream().map(userConverter::forInvite).collect(Collectors.toList()))
                .build();
    }

    private boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        boolean isMimeTypeImage = isImageFileMine(file);
        boolean isExtensionImage = isImageFileExtension(file);
        boolean isContentValidImage = isValidImageFile(file);

        return isMimeTypeImage && isExtensionImage && isContentValidImage;
    }

    private boolean isImageFileMine(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private boolean isImageFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            return fileName.toLowerCase().matches(".*\\.(png|jpg|jpeg|gif|bmp|webp)$");
        }
        return false;
    }

    private boolean isValidImageFile(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }
}
