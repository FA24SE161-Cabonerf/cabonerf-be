package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UserConverter;
import com.example.cabonerfbe.dto.UserAdminDto;
import com.example.cabonerfbe.dto.UserProfileDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.models.UserOrganization;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.repositories.OrganizationRepository;
import com.example.cabonerfbe.repositories.UserOrganizationRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.request.UpdateUserInfoRequest;
import com.example.cabonerfbe.response.*;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.services.S3Service;
import com.example.cabonerfbe.services.UserService;
import com.example.cabonerfbe.util.FileUtil;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The class User service.
 *
 * @author SonPHH.
 */
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

    @Autowired
    FileUtil fileUtil;

    @Autowired
    UserOrganizationRepository uoRepository;

    @Autowired
    OrganizationRepository oRepository;

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
                : userRepository.findAllExceptAdminAndManager(pageable);

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
        if (!fileUtil.isImageFile(file)) {
            throw CustomExceptions.badRequest("Invalid image.");
        }

        if (u.getProfilePictureUrl() != null && !Constants.DEFAULT_USER_IMAGE.equals(u.getProfilePictureUrl())) {
            s3Service.deleteFile(u.getProfilePictureUrl());
        }
        try {
            u.setProfilePictureUrl(s3Service.uploadImage(file));
        } catch (Exception ignored) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_UPLOAD_IMAGE);
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

    @Override
    public List<UserDashboardResponse> getNewUserInThisYear() {

        int currentYear = LocalDate.now().getYear();

        List<String> months = Stream.of(Month.values())
                .map(Month::name)
                .collect(Collectors.toList());

        return months.stream()
                .map(month -> {

                    int monthNumber = Month.valueOf(month).getValue();

                    return new UserDashboardResponse(month, userRepository.countByCreatedAtMonthAndYear(monthNumber, currentYear));
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDto updateProfile(UUID userId, UpdateUserInfoRequest request) {
        Users user = userRepository.findByIdWithStatus(userId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.USER_NOT_FOUND)
        );

        if (Stream.of(request.getFullName(), request.getBio(), request.getPhone(), request.getProfilePictureUrl())
                .allMatch(String::isEmpty)) {
            throw CustomExceptions.badRequest("Update at least one field");
        }

        user.setFullName(Optional.ofNullable(request.getFullName()).filter(s -> !s.isEmpty()).orElse(user.getFullName()));
        user.setBio(Optional.ofNullable(request.getBio()).filter(s -> !s.isEmpty()).orElse(user.getBio()));
        user.setPhone(Optional.ofNullable(request.getPhone()).filter(s -> !s.isEmpty()).orElse(user.getPhone()));
        user.setProfilePictureUrl(Optional.ofNullable(request.getProfilePictureUrl())
                .filter(s -> !s.isEmpty())
                .orElse(user.getProfilePictureUrl()));

        List<UserOrganization> uo = uoRepository.getByUser(userId);
        Organization o = new Organization();
        for (UserOrganization x : uo) {
            if (x.getOrganization().getContract() == null && Objects.equals(x.getRole().getName(), Constants.ORGANIZATION_MANAGER)) {
                o = x.getOrganization();
            }
        }
        String[] _name = user.getFullName().split(" ");
        o.setName(_name[_name.length - 1] + "'s Organization");

        return userConverter.fromUserToUserProfileDto(userRepository.save(user));
    }

    @Override
    public int countAllUser() {
        return userRepository.findAll().size();
    }


}
