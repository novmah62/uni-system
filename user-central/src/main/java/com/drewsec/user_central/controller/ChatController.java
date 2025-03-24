package com.drewsec.user_central.controller;

import com.drewsec.user_central.definitions.constants.ApiMessageConstants;
import com.drewsec.user_central.dto.response.ApiResponse;
import com.drewsec.user_central.dto.response.ChatResponse;
import com.drewsec.user_central.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@Tag(name = "chat", description = "Chat Endpoints")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ChatResponse>> getChatsByReceiver() {
        return new ApiResponse<>(200,
                ApiMessageConstants.CHAT_FOUND,
                chatService.getChatsByReceiverId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> createChat(
            @RequestParam(name = "sender-id") String senderId,
            @RequestParam(name = "receiver-id") String receiverId) {
        return new ApiResponse<>(201,
                ApiMessageConstants.CHAT_CREATED,
                chatService.createChat(senderId, receiverId));
    }

}
