package com.withquery.webide_spring_be.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "에러 응답")
public class ErrorResponse {

	@Schema(description = "에러 코드", example = "DUPLICATE_EMAIL")
	private String code;

	@Schema(description = "에러 메시지", example = "이미 존재하는 이메일입니다.")
	private String message;

	@Schema(description = "에러 발생 시간", example = "2025-07-20T11:55:35.423")
	private LocalDateTime timestamp;

	public ErrorResponse(String code, String message) {
		this.code = code;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}
} 