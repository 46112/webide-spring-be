package com.withquery.webide_spring_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 수정 요청")
public record UserUpdateRequest(
    @Schema(description = "새로운 닉네임", example = "newNickname")
    String nickname
) {} 