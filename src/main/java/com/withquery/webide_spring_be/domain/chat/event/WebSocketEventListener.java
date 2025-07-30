package com.withquery.webide_spring_be.domain.chat.event;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.withquery.webide_spring_be.domain.chat.dto.ChatMessage;
import com.withquery.webide_spring_be.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String userEmail = (String) headerAccessor.getSessionAttributes().get("userEmail");
        Long projectId = (Long) headerAccessor.getSessionAttributes().get("projectId");

        if (username != null && projectId != null) {
            log.info("User disconnected: {} from project: {}", username, projectId);

            ChatMessage chatMessage = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .senderEmail(userEmail)
                    .senderNickname(username)
                    .projectId(projectId)
                    .content(username + "님이 채팅방을 나갔습니다.")
                    .build();

            ChatMessage savedMessage = chatService.processMessage(chatMessage);
            
            messagingTemplate.convertAndSend("/topic/project/" + projectId, savedMessage);
        }
    }
} 