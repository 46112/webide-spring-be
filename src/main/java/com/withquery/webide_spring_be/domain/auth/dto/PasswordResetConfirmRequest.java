package com.withquery.webide_spring_be.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 재설정 확인 요청")
public record PasswordResetConfirmRequest(
    @Schema(description = "비밀번호 재설정 토큰", example = "eyJhbGciOiJIUzM4NCJ9...")
    String resetToken,
    @Schema(description = "새로운 비밀번호", example = "newPassword123")
    String newPassword
) {} 