package com.drewsec.user_central.service.impl;

import com.drewsec.user_central.definitions.enumType.MessageState;
import com.drewsec.user_central.definitions.enumType.MessageType;
import com.drewsec.user_central.definitions.enumType.NotificationType;
import com.drewsec.user_central.dto.NotificationDto;
import com.drewsec.user_central.dto.request.MessageRequest;
import com.drewsec.user_central.dto.response.MessageResponse;
import com.drewsec.user_central.entity.Chat;
import com.drewsec.user_central.entity.Message;
import com.drewsec.user_central.exception.ResourceNotFoundException;
import com.drewsec.user_central.mapper.MessageMapper;
import com.drewsec.user_central.repository.ChatRepository;
import com.drewsec.user_central.repository.MessageRepository;
import com.drewsec.user_central.service.FileService;
import com.drewsec.user_central.service.MessageService;
import com.drewsec.user_central.service.NotificationService;
import com.drewsec.user_central.service.UserService;
import com.drewsec.user_central.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper mapper;
    private final NotificationService notificationService;
    private final FileService fileService;
    private final UserService userService;

    @Override
    public void saveMessage(MessageRequest messageRequest) {
        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new ResourceNotFoundException("Chat", "chat ID", messageRequest.getChatId()));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setType(messageRequest.getType());
        message.setState(MessageState.SENT);

        messageRepository.save(message);

        NotificationDto notification = NotificationDto.builder()
                .chatId(chat.getId())
                .messageType(messageRequest.getType())
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .receiverId(messageRequest.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getTargetChatName(message.getSenderId()))
                .build();

        notificationService.sendNotification(messageRequest.getReceiverId(), notification);
    }

    @Override
    public List<MessageResponse> findChatMessages(String chatId) {
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(mapper::toMessageResponse)
                .toList();
    }

    @Override
    @Transactional
    public void setMessagesToSeen(String chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat", "chat ID", chatId));
        final String recipientId = getRecipientId(chat);

        messageRepository.setMessagesToSeenByChatId(chatId, MessageState.SEEN);

        NotificationDto notification = NotificationDto.builder()
                .chatId(chat.getId())
                .type(NotificationType.SEEN)
                .receiverId(recipientId)
                .senderId(getSenderId(chat))
                .build();

        notificationService.sendNotification(recipientId, notification);
    }

    @Override
    public void uploadMediaMessage(String chatId, MultipartFile file) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat", "chat ID", chatId));

        final String senderId = getSenderId(chat);
        final String receiverId = getRecipientId(chat);

        final String filePath = fileService.saveFile(file, senderId);
        Message message = new Message();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setState(MessageState.SENT);
        message.setType(MessageType.IMAGE);
        message.setMediaFilePath(filePath);
        message.setChat(chat);
        messageRepository.save(message);

        NotificationDto notification = NotificationDto.builder()
                .chatId(chat.getId())
                .type(NotificationType.IMAGE)
                .senderId(senderId)
                .receiverId(receiverId)
                .messageType(MessageType.IMAGE)
                .media(FileUtils.readFileFromLocation(filePath))
                .build();

        notificationService.sendNotification(receiverId, notification);
    }

    private String getSenderId(Chat chat) {
        final String userId = userService.getUserAuthenticationName();
        if (chat.getSender().getId().equals(userId)) {
            return chat.getSender().getId();
        }
        return chat.getRecipient().getId();
    }

    private String getRecipientId(Chat chat) {
        final String userId = userService.getUserAuthenticationName();
        if (chat.getSender().getId().equals(userId)) {
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }

}
