package com.withquery.webide_spring_be.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 재설정 요청 응답")
public record PasswordResetRequestResponse(
    @Schema(description = "비밀번호 재설정 토큰", example = "eyJhbGciOiJIUzM4NCJ9...")
    String resetToken,
    @Schema(description = "응답 메시지", example = "비밀번호 재설정 토큰이 발급되었습니다.")
    String message
) {} 