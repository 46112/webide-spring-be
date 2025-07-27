package com.withquery.webide_spring_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 변경 응답")
public record PasswordChangeResponse(
    @Schema(description = "응답 메시지", example = "비밀번호가 성공적으로 변경되었습니다.")
    String message
) {} 