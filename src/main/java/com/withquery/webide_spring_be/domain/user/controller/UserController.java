package com.withquery.webide_spring_be.domain.user.controller;

import com.withquery.webide_spring_be.common.dto.ErrorResponse;
import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationRequest;
import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationResponse;
import com.withquery.webide_spring_be.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

	private final UserService userService;

	@PostMapping("/register")
	@Operation(
		summary = "회원가입",
		description = "새로운 사용자를 등록합니다. 이메일과 닉네임은 중복될 수 없습니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "회원가입 성공",
			content = @Content(
				schema = @Schema(implementation = UserRegistrationResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (중복된 이메일 또는 닉네임)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<UserRegistrationResponse> registerUser(
		@RequestBody UserRegistrationRequest request) {
		UserRegistrationResponse response = userService.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
} 