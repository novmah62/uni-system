package com.drewsec.user_central.dto;

import com.drewsec.user_central.definitions.enumType.MessageType;
import com.drewsec.user_central.definitions.enumType.NotificationType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {

    private String chatId;
    private String content;
    private String senderId;
    private String receiverId;
    private String chatName;
    private MessageType messageType;
    private NotificationType type;
    private byte[] media;

}
