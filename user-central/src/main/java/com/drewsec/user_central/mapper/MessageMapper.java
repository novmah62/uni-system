package com.drewsec.user_central.mapper;

import com.drewsec.user_central.entity.Message;
import com.drewsec.user_central.dto.response.MessageResponse;
import com.drewsec.user_central.utils.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .type(message.getType())
                .state(message.getState())
                .createdAt(message.getCreatedDate())
                .media(FileUtils.readFileFromLocation(message.getMediaFilePath()))
                .build();
    }
}
