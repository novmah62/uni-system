package kma.ktlt.post.domain.notification.dto;

import lombok.Data;

@Data
public class NotificationCommentPostOwnerRequest {
    String senderId;
    String content;
    Long postId;

    String postOwnerId;

    int currentUserCommentNotReplyUser;
}
