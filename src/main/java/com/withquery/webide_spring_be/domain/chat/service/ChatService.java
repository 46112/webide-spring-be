package com.withquery.webide_spring_be.domain.chat.service;

import java.util.List;

import com.withquery.webide_spring_be.domain.chat.dto.ChatMessage;

public interface ChatService {
    
    ChatMessage processMessage(ChatMessage chatMessage);
    
    List<ChatMessage> getChatHistory(Long projectId);
    
    List<ChatMessage> getRecentMessages(Long projectId, int limit);
    
    List<ChatMessage> getMessagesAfterTime(Long projectId, java.time.LocalDateTime afterTime);
} 