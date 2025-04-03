package com.drewsec.user_central.service.impl;

import com.drewsec.user_central.dto.UserDto;
import com.drewsec.user_central.entity.User;
import com.drewsec.user_central.mapper.UserMapper;
import com.drewsec.user_central.repository.UserRepository;
import com.drewsec.user_central.service.UserSyncService;
import com.drewsec.user_central.utils.CacheUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSyncServiceImpl implements UserSyncService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CacheUtil cacheUtil;

//    @Override
//    public void synchronizeWithIdp(Jwt token) {
//        log.info("Synchronizing user with idp");
//        getUserEmail(token).ifPresent(userEmail -> {
//            log.info("Synchronizing user having email {}", userEmail);
//            Optional<User> optUser = userRepository.findByEmail(userEmail);
//            User user = userMapper.fromTokenAttributes(token.getClaims());
//            optUser.ifPresent(value -> user.setId(value.getId()));
//            userRepository.save(user);
//            cacheUtil.saveUser(userMapper.toUserDto(user));
//        });
//
//    }
@Override
public void synchronizeWithIdp(Jwt token) {
    log.info("Synchronizing user with idp");
    getUserEmail(token).ifPresent(userEmail -> {
        log.info("Synchronizing user having email {}", userEmail);
        Optional<User> optUser = userRepository.findByEmail(userEmail);
        // Lấy giá trị của claim "sub" từ token
        String userId = token.getClaim("sub");
        // Tạo đối tượng user từ các thuộc tính của token
        User user = userMapper.fromTokenAttributes(token.getClaims());
        // Đặt id của user bằng giá trị của "sub"
        user.setId(userId);
        // Nếu người dùng đã tồn tại, giữ lại id của người dùng đã có
        optUser.ifPresent(existingUser -> user.setId(existingUser.getId()));
        userRepository.save(user);
        cacheUtil.saveUser(userMapper.toUserDto(user));
    });
}

    @Override
    @PostConstruct
    public void synchronizeWithCache() {
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .toList();

        if (!users.isEmpty()) {
            cacheUtil.saveAllUsers(users);
            log.info("Users loaded into Redis Cache!");
        }
    }

    public Optional<String> getUserEmail(Jwt token) {
        Map<String, Object> attributes = token.getClaims();
        if (attributes.containsKey("email")) {
            return Optional.of(attributes.get("email").toString());
        }
        return Optional.empty();
    }

}
