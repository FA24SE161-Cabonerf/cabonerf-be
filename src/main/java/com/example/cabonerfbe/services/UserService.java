package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UserAdminDto;
import com.example.cabonerfbe.response.GetAllUserResponse;
import com.example.cabonerfbe.response.GetProfileResponse;

import java.util.UUID;

public interface UserService {
    GetProfileResponse getMe(UUID userId);
    GetAllUserResponse getAll(int pageCurrent, int pageSize, String keyword);
    UserAdminDto updateUserStatus(UUID userId);
}
