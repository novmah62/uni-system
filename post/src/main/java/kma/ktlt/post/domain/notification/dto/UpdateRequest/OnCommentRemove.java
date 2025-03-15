package kma.ktlt.post.domain.notification.dto.UpdateRequest;

import kma.ktlt.post.domain.post.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class OnCommentRemove {
    Long commentId;
    Long postId;
    Long parentCommentId;
    Integer currentUserComment; //ngoại trừ chủ bài viết nếu là bình luận trực tiếp hoặc chủ bình luận cha nếu là trả lời
    String latestCommenterId;

}
