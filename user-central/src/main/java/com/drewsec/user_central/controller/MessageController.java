package com.drewsec.user_central.controller;

import com.drewsec.user_central.definitions.constants.ApiMessageConstants;
import com.drewsec.user_central.dto.request.MessageRequest;
import com.drewsec.user_central.dto.response.ApiResponse;
import com.drewsec.user_central.dto.response.MessageResponse;
import com.drewsec.user_central.service.MessageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@Tag(name = "message", description = "Message Endpoints")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> saveMessage(@RequestBody MessageRequest message) {
        messageService.saveMessage(message);
        return new ApiResponse<>(201, ApiMessageConstants.MESSAGE_SENT);
    }

    @PostMapping(value = "/upload-media", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> uploadMedia(
            @RequestParam("chat-id") String chatId,
            @Parameter()
            @RequestPart("file") MultipartFile file
    ) {
        messageService.uploadMediaMessage(chatId, file);
        return new ApiResponse<>(201, ApiMessageConstants.MESSAGE_SENT);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse<String> setMessageToSeen(@RequestParam("chat-id") String chatId) {
        messageService.setMessagesToSeen(chatId);
        return new ApiResponse<>(202, ApiMessageConstants.MESSAGE_UPDATED);
    }

    @GetMapping("/chat/{chat-id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<MessageResponse>> getAllMessages(
            @PathVariable("chat-id") String chatId
    ) {
        return new ApiResponse<>(200,
                ApiMessageConstants.MESSAGE_FOUND,
                messageService.findChatMessages(chatId));
    }

}
