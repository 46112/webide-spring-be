package com.withquery.webide_spring_be.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(
    @Schema(description = "성공 메시지", example = "로그인이 성공했습니다.")
    String message,
    
    @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    String token,
    
    @Schema(description = "사용자 닉네임", example = "username")
    String nickname
) {} 