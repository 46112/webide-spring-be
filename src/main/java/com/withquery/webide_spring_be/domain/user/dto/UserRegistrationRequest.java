package com.withquery.webide_spring_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 요청")
public record UserRegistrationRequest(
    @Schema(description = "이메일", example = "user@example.com", required = true)
    String email,
    
    @Schema(description = "닉네임", example = "username", required = true)
    String nickname,
    
    @Schema(description = "비밀번호", example = "password123", required = true)
    String password
) {} 