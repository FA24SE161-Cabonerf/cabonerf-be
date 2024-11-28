package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UserAdminDto;
import com.example.cabonerfbe.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    GetProfileResponse getMe(UUID userId);

    GetAllUserResponse getAll(int pageCurrent, int pageSize, String keyword);

    UserAdminDto updateUserStatus(UUID userId);

    UpdateAvatarUserResponse updateAvatarUser(UUID userId, MultipartFile file);

    GetUserToInviteResponse getToInvite(int pageCurrent, int pageSize, String keyword);

    List<UserDashboardResponse> getNewUserInThisYear();

    int countAllUser();
}
