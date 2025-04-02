package kma.ktlt.post.domain.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    public Optional<UserDTO> getUserById(String userId) {
        String userJson = redisTemplate.opsForValue().get("user:" + userId);
        if (userJson == null) return Optional.empty();

        try {
            return Optional.of(objectMapper.readValue(userJson, UserDTO.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<UserDTO> getAll() {
        // Lấy danh sách tất cả các key có dạng "user:*"
        Set<String> keys = redisTemplate.keys("user:*");
        if (keys == null || keys.isEmpty()) return List.of();

        // Lấy toàn bộ dữ liệu từ Redis theo danh sách key
        List<String> userJsonList = redisTemplate.opsForValue().multiGet(keys);
        if (userJsonList == null) return List.of();

        // Chuyển đổi JSON thành UserDTO
        return userJsonList.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, UserDTO.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(user -> user != null) // Bỏ qua giá trị null
                .collect(Collectors.toList());
    }


}
