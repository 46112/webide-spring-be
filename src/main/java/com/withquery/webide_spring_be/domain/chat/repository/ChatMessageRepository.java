package com.withquery.webide_spring_be.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.withquery.webide_spring_be.domain.chat.entity.ChatMessageEntity;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    
    /**
     * 프로젝트의 최근 채팅 메시지들을 시간순으로 조회
     * @param projectId 프로젝트 ID
     * @param limit 조회할 메시지 개수 (최신순)
     * @return 채팅 메시지 목록
     */
    @Query("SELECT c FROM ChatMessageEntity c WHERE c.projectId = :projectId ORDER BY c.createdAt DESC")
    List<ChatMessageEntity> findRecentMessagesByProjectId(@Param("projectId") Long projectId);
    
    /**
     * 프로젝트의 모든 채팅 메시지를 시간순으로 조회
     * @param projectId 프로젝트 ID
     * @return 채팅 메시지 목록
     */
    @Query("SELECT c FROM ChatMessageEntity c WHERE c.projectId = :projectId ORDER BY c.createdAt ASC")
    List<ChatMessageEntity> findAllMessagesByProjectId(@Param("projectId") Long projectId);
    
    /**
     * 특정 시간 이후의 메시지들을 조회
     * @param projectId 프로젝트 ID
     * @param afterTime 기준 시간
     * @return 채팅 메시지 목록
     */
    @Query("SELECT c FROM ChatMessageEntity c WHERE c.projectId = :projectId AND c.createdAt > :afterTime ORDER BY c.createdAt ASC")
    List<ChatMessageEntity> findMessagesAfterTime(@Param("projectId") Long projectId, @Param("afterTime") java.time.LocalDateTime afterTime);
} 