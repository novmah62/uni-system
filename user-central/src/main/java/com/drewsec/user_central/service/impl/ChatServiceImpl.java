package com.drewsec.user_central.service.impl;

import com.drewsec.user_central.dto.response.ChatResponse;
import com.drewsec.user_central.entity.Chat;
import com.drewsec.user_central.entity.User;
import com.drewsec.user_central.exception.ResourceNotFoundException;
import com.drewsec.user_central.mapper.ChatMapper;
import com.drewsec.user_central.repository.ChatRepository;
import com.drewsec.user_central.repository.UserRepository;
import com.drewsec.user_central.service.ChatService;
import com.drewsec.user_central.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper mapper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReceiverId() {
        final String userId = userService.getUserAuthenticationName();
        return chatRepository.findChatsBySenderId(userId)
                .stream()
                .map(c -> mapper.toChatResponse(c, userId))
                .toList();
    }

    @Override
    public String createChat(String senderId, String receiverId) {
        Optional<Chat> existingChat = chatRepository.findChatByReceiverAndSender(senderId, receiverId);
        if (existingChat.isPresent()) {
            return existingChat.get().getId();
        }

        User sender = userRepository.findByPublicId(senderId)
                .orElseThrow(() ->  new ResourceNotFoundException("User", "sender ID", senderId));
        User receiver = userRepository.findByPublicId(receiverId)
                .orElseThrow(() ->  new ResourceNotFoundException("User", "receiver ID", receiverId));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setRecipient(receiver);

        Chat savedChat = chatRepository.save(chat);
        return savedChat.getId();
    }

}
