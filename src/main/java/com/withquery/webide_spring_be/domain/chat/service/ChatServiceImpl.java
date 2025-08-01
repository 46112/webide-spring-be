package com.withquery.webide_spring_be.domain.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.withquery.webide_spring_be.domain.chat.dto.ChatMessage;
import com.withquery.webide_spring_be.domain.chat.entity.ChatMessageEntity;
import com.withquery.webide_spring_be.domain.chat.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage processMessage(ChatMessage chatMessage) {
        chatMessage.setId(UUID.randomUUID().toString());
        
        chatMessage.setTimestamp(LocalDateTime.now());
        
        if (chatMessage.getType() == null) {
            chatMessage.setType(ChatMessage.MessageType.CHAT);
        }
        
        ChatMessageEntity entity = ChatMessageEntity.builder()
            .messageId(chatMessage.getId())
            .projectId(chatMessage.getProjectId())
            .senderEmail(chatMessage.getSenderEmail())
            .senderNickname(chatMessage.getSenderNickname())
            .content(chatMessage.getContent())
            .messageType(chatMessage.getType())
            .createdAt(chatMessage.getTimestamp())
            .build();
        
        chatMessageRepository.save(entity);
        
        return chatMessage;
    }

    @Override
    public List<ChatMessage> getChatHistory(Long projectId) {
        List<ChatMessageEntity> entities = chatMessageRepository.findAllMessagesByProjectId(projectId);
        return entities.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessage> getRecentMessages(Long projectId, int limit) {
        List<ChatMessageEntity> entities = chatMessageRepository.findRecentMessagesByProjectId(projectId);
        
        return entities.stream()
            .limit(limit)
            .sorted((e1, e2) -> e1.getCreatedAt().compareTo(e2.getCreatedAt()))
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessage> getMessagesAfterTime(Long projectId, LocalDateTime afterTime) {
        List<ChatMessageEntity> entities = chatMessageRepository.findMessagesAfterTime(projectId, afterTime);
        return entities.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    private ChatMessage convertToDto(ChatMessageEntity entity) {
        return ChatMessage.builder()
            .id(entity.getMessageId())
            .projectId(entity.getProjectId())
            .senderEmail(entity.getSenderEmail())
            .senderNickname(entity.getSenderNickname())
            .content(entity.getContent())
            .type(entity.getMessageType())
            .timestamp(entity.getCreatedAt())
            .build();
    }
} 