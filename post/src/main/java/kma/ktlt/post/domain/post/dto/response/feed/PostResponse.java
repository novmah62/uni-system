package kma.ktlt.post.domain.post.dto.response.feed;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    private Long postId;
    private String userId;
    private String content;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime updatedAt;
    private boolean isUpdated;
    private UserResponse userInfo;


}
//
