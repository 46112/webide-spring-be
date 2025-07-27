package com.withquery.webide_spring_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 수정 응답")
public record UserUpdateResponse(
    @Schema(description = "수정된 닉네임", example = "newNickname")
    String nickname,
    @Schema(description = "변경된 닉네임을 반영한 새로운 JWT 토큰", example = "eyJhbGciOiJIUzM4NCJ9...")
    String token,
    @Schema(description = "수정 완료 메시지", example = "사용자 정보가 수정되었습니다.")
    String message
) {} 