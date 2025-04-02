package kma.ktlt.post.app.api;

import kma.ktlt.post.domain.common.PageResponse;
import kma.ktlt.post.domain.common.ResponseData;
import kma.ktlt.post.domain.post.dto.response.feed.CommentResponse;
import kma.ktlt.post.domain.user.RedisService;
import kma.ktlt.post.domain.user.UserDTO;
import kma.ktlt.post.domain.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private RedisService redisService;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseData<?> getUserFromRedis() {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy user thành công",
                LocalDateTime.now(),
                redisService.getUserById(userService.getUserAuthenticationName())
        );
    }

}