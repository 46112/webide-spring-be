package com.withquery.webide_spring_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답")
<<<<<<< HEAD
public class UserRegistrationResponse {

	@Schema(description = "성공 메시지", example = "회원가입이 완료되었습니다.")
	private String message;

	@Schema(description = "사용자 ID", example = "1")
	private Long userId;

	public UserRegistrationResponse(String message, Long userId) {
		this.message = message;
		this.userId = userId;
	}
} 
=======
public record UserRegistrationResponse(
    @Schema(description = "성공 메시지", example = "회원가입이 완료되었습니다.")
    String message,
    
    @Schema(description = "사용자 ID", example = "1")
    Long userId
) {} 
>>>>>>> main
