package kma.ktlt.post.domain.notification.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationCommentCreatedToOtherComment1stRequest {

    String senderId;
    String content;
    Long postId;
    int currentUserCommentNotReplyUser;
    List<String> otherCommenterNotPostOwner;
}
