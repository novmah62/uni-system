package kma.ktlt.post.domain.notification.dto;

import lombok.Data;

@Data
public class NotificationPostRequest {
    String senderId;
    String content;
    Long postId;
}
