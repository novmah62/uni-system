package kma.ktlt.post.domain.post.dto.response.feed;

import jakarta.persistence.*;
import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@Setter
public class CommentResponse {
    private Long id;

    private String content;

    private Long postId;

    private String userId;

    private boolean isUpdated;

    private Long parentCommentId;

    private LocalDateTime updatedAt;

    private UserResponse userInfo;


    public CommentResponse(Long id, String content, Long postId, String userId, boolean isUpdated, Long parentCommentId, LocalDateTime updatedAt, UserResponse userInfo) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
        this.isUpdated = isUpdated;
        this.parentCommentId = parentCommentId;
        this.updatedAt = updatedAt;
        this.userInfo = userInfo;
    }


}
