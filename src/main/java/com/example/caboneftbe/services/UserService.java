package com.example.caboneftbe.services;

import com.example.caboneftbe.response.GetProfileResponse;

public interface UserService {
    GetProfileResponse getMe(String accessToken);
}
