package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UserAdminDto;
import com.example.cabonerfbe.dto.UserProfileDto;
import com.example.cabonerfbe.request.UpdateUserInfoRequest;
import com.example.cabonerfbe.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * The interface User service.
 *
 * @author SonPHH.
 */
public interface UserService {
    /**
     * Gets me.
     *
     * @param userId the user id
     * @return the me
     */
    GetProfileResponse getMe(UUID userId);

    /**
     * Gets all.
     *
     * @param pageCurrent the page current
     * @param pageSize    the page size
     * @param keyword     the keyword
     * @return the all
     */
    GetAllUserResponse getAll(int pageCurrent, int pageSize, String keyword);

    /**
     * Update user status method.
     *
     * @param userId the user id
     * @return the user admin dto
     */
    UserAdminDto updateUserStatus(UUID userId);

    /**
     * Update avatar user method.
     *
     * @param userId the user id
     * @param file   the file
     * @return the update avatar user response
     */
    UpdateAvatarUserResponse updateAvatarUser(UUID userId, MultipartFile file);

    /**
     * Gets to invite.
     *
     * @param pageCurrent the page current
     * @param pageSize    the page size
     * @param keyword     the keyword
     * @return the to invite
     */
    GetUserToInviteResponse getToInvite(int pageCurrent, int pageSize, String keyword);

    /**
     * Gets new user in this year.
     *
     * @return the new user in this year
     */
    List<UserDashboardResponse> getNewUserInThisYear();

    /**
     * Update profile method.
     *
     * @param userId  the user id
     * @param request the request
     * @return the user profile dto
     */
    UserProfileDto updateProfile(UUID userId, UpdateUserInfoRequest request);

    /**
     * Count all user method.
     *
     * @return the int
     */
    int countAllUser();


}
