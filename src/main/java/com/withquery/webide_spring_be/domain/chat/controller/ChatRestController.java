package com.withquery.webide_spring_be.domain.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.withquery.webide_spring_be.domain.chat.dto.ChatMessage;
import com.withquery.webide_spring_be.domain.chat.service.ChatService;
import com.withquery.webide_spring_be.domain.project.service.ProjectMemberService;
import com.withquery.webide_spring_be.domain.project.entity.ProjectMemberRole;
import com.withquery.webide_spring_be.util.jwt.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@Tag(name = "Chat", description = "채팅 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/projects/{projectId}/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;
    private final ProjectMemberService projectMemberService;

    @Operation(summary = "채팅 히스토리 조회", description = "프로젝트의 전체 채팅 히스토리를 조회합니다.")
    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            Authentication authentication) {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getEmail();

        if (!projectMemberService.hasPermission(projectId, userEmail, ProjectMemberRole.MEMBER, ProjectMemberRole.OWNER)) {
            throw new RuntimeException("프로젝트 채팅 히스토리를 조회할 권한이 없습니다.");
        }

        List<ChatMessage> chatHistory = chatService.getChatHistory(projectId);
        return ResponseEntity.ok(chatHistory);
    }

    @Operation(summary = "최근 채팅 메시지 조회", description = "프로젝트의 최근 채팅 메시지들을 조회합니다.")
    @GetMapping("/recent")
    public ResponseEntity<List<ChatMessage>> getRecentMessages(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "조회할 메시지 개수 (기본값: 50)") @RequestParam(defaultValue = "50") int limit,
            Authentication authentication) {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getEmail();

        if (!projectMemberService.hasPermission(projectId, userEmail, ProjectMemberRole.MEMBER, ProjectMemberRole.OWNER)) {
            throw new RuntimeException("프로젝트 채팅 메시지를 조회할 권한이 없습니다.");
        }

        List<ChatMessage> recentMessages = chatService.getRecentMessages(projectId, limit);
        return ResponseEntity.ok(recentMessages);
    }
} 