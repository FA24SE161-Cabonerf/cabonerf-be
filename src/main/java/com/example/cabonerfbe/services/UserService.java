package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.GetProfileResponse;

public interface UserService {
    GetProfileResponse getMe(String accessToken);
}
