package com.drewsec.user_central.service;

import com.drewsec.user_central.entity.Chat;
import com.drewsec.user_central.dto.request.MessageRequest;
import com.drewsec.user_central.dto.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {

    void saveMessage(MessageRequest messageRequest);
    List<MessageResponse> findChatMessages(String chatId);
    void setMessagesToSeen(String chatId);
    void uploadMediaMessage(String chatId, MultipartFile file);

}
