package com.withquery.webide_spring_be.domain.chat.controller;

import com.withquery.webide_spring_be.domain.chat.dto.ChatMessage;
import com.withquery.webide_spring_be.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        log.info("채팅 메시지 수신: {} -> 프로젝트 {}", chatMessage.getSenderNickname(), chatMessage.getProjectId());
        
        if (chatMessage.getType() == null) {
            chatMessage.setType(ChatMessage.MessageType.CHAT);
        }
        
        ChatMessage processedMessage = chatService.processMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/project/" + chatMessage.getProjectId(), processedMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderNickname());
        headerAccessor.getSessionAttributes().put("userEmail", chatMessage.getSenderEmail());
        headerAccessor.getSessionAttributes().put("projectId", chatMessage.getProjectId());
        
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        
        log.info("채팅방 입장: {} -> 프로젝트 {}", chatMessage.getSenderNickname(), chatMessage.getProjectId());
        ChatMessage processedMessage = chatService.processMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/project/" + chatMessage.getProjectId(), processedMessage);
    }
} 