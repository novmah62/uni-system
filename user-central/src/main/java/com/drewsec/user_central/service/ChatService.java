package com.drewsec.user_central.service;

import com.drewsec.user_central.dto.response.ChatResponse;

import java.util.List;

public interface ChatService {

    List<ChatResponse> getChatsByReceiverId();
    String createChat(String senderId, String receiverId);

}
