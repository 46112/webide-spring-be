package com.withquery.webide_spring_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 변경 요청")
public record PasswordChangeRequest(
    @Schema(description = "현재 비밀번호", example = "currentPassword123")
    String currentPassword,
    @Schema(description = "새로운 비밀번호", example = "newPassword123")
    String newPassword
) {} 