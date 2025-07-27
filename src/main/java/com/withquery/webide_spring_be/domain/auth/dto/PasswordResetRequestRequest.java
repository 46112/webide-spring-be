package com.withquery.webide_spring_be.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 재설정 요청")
public record PasswordResetRequestRequest(
    @Schema(description = "사용자 이메일", example = "user@example.com")
    String email
) {} 