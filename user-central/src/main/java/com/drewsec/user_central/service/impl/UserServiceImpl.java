package com.drewsec.user_central.service.impl;

import com.drewsec.user_central.dto.UserDto;
import com.drewsec.user_central.dto.response.UserResponse;
import com.drewsec.user_central.mapper.UserMapper;
import com.drewsec.user_central.repository.UserRepository;
import com.drewsec.user_central.service.UserService;
import com.drewsec.user_central.utils.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CacheUtil cacheUtil;

    @Override
    public List<UserResponse> finAllUsersExceptSelf() {
        return userRepository.findAllUsersExceptSelf(getUserAuthenticationName())
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public String getUserAuthenticationName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public UserDto getUserById(String userId) {
        UserDto cachedUser = cacheUtil.getUserById(userId);
        if (cachedUser != null) {
            log.info("User fetched from Redis Cache!");
            return cachedUser;
        }

        log.info("User fetched from Database!");
        return userRepository.findById(userId)
                .map(user -> {
                    UserDto userDTO = userMapper.toUserDto(user);
                    cacheUtil.saveUser(userDTO);
                    return userDTO;
                })
                .orElse(null);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> cachedUsers = cacheUtil.getAllUsers();
        if (!cachedUsers.isEmpty()) {
            log.info("Users fetched from Redis Cache!");
            return cachedUsers;
        }

        log.info("Users fetched from Database!");
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .toList();

        cacheUtil.saveAllUsers(users);
        return users;
    }

}
