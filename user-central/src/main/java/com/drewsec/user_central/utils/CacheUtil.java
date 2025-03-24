package com.drewsec.user_central.utils;

import com.drewsec.user_central.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class CacheUtil {

    private static final String USER_KEY_PREFIX = "user:";
    private static final long TTL = 1; // 1h
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public CacheUtil(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void saveUser(UserDto user) {
        try {
            String key = USER_KEY_PREFIX + user.getId();
            String userJson = objectMapper.writeValueAsString(user);
            redisTemplate.opsForValue().set(key, userJson, TTL, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void saveAllUsers(List<UserDto> users) {
        users.forEach(this::saveUser);
    }

    public void deleteUser(String userId) {
        String key = USER_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }

    public void clearCache() {
        Set<String> keys = redisTemplate.keys(USER_KEY_PREFIX + "*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public UserDto getUserById(String userId) {
        String key = USER_KEY_PREFIX + userId;
        String userJson = redisTemplate.opsForValue().get(key);

        try {
            redisTemplate.expire(key, TTL, TimeUnit.HOURS);
            return objectMapper.readValue(userJson, UserDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<UserDto> getAllUsers() {
        Set<String> keys = redisTemplate.keys(USER_KEY_PREFIX + "*");
        if (keys.isEmpty()) return List.of();

        List<String> userJsonList = redisTemplate.opsForValue().multiGet(keys);

        return userJsonList.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, UserDto.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
