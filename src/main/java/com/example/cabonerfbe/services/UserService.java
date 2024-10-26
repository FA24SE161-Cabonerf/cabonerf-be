package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.GetProfileResponse;

import java.util.UUID;

public interface UserService {
    GetProfileResponse getMe(UUID userId);
}
