package com.drewsec.user_central.service;

import com.drewsec.user_central.dto.UserDto;
import com.drewsec.user_central.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> finAllUsersExceptSelf();
    String getUserAuthenticationName();
    UserDto getUserById(String userId);
    List<UserDto> getAllUsers();

}
