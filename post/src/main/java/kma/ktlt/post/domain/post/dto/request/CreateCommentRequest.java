package kma.ktlt.post.domain.post.dto.request;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String content;
    private Long postId;
    private Long parentCommentId;
}
