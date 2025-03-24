package com.drewsec.user_central.dto.response;

import com.drewsec.user_central.definitions.enumType.MessageState;
import com.drewsec.user_central.definitions.enumType.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private Long id;
    private String content;
    private MessageType type;
    private MessageState state;
    private String senderId;
    private String receiverId;
    private LocalDateTime createdAt;
    private byte[] media;

}

