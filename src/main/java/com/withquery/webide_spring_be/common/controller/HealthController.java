package com.withquery.webide_spring_be.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check", description = "애플리케이션 상태 확인 API")
public class HealthController {

	@GetMapping("/health")
	@Operation(
		summary = "헬스체크",
		description = "애플리케이션의 현재 상태를 확인합니다. 정상 동작 시 'OK'를 반환합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "애플리케이션 정상 동작",
			content = @Content(
				mediaType = "text/plain",
				examples = @ExampleObject(
					value = "OK",
					summary = "정상 응답 예시"
				)
			)
		),
		@ApiResponse(
			responseCode = "503",
			description = "애플리케이션 비정상 상태",
			content = @Content(
				mediaType = "text/plain",
				examples = @ExampleObject(
					value = "Service Unavailable",
					summary = "오류 응답 예시"
				)
			)
		)
	})
	public String health() {
		return "OK";
	}
} 