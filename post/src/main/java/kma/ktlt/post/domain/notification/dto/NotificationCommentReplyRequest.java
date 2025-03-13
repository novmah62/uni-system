package kma.ktlt.post.domain.notification.dto;

import lombok.Data;

@Data
public class NotificationCommentReplyRequest {
    Long parentCommentId;
    String senderId;
    String receiverId;
    String commentContent;
    int currentUserReply1st;

}
