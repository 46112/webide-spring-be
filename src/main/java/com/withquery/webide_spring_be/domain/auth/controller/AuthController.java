package com.withquery.webide_spring_be.domain.auth.controller;

import com.withquery.webide_spring_be.common.dto.ErrorResponse;
import com.withquery.webide_spring_be.domain.auth.dto.LoginRequest;
import com.withquery.webide_spring_be.domain.auth.dto.LoginResponse;
import com.withquery.webide_spring_be.domain.auth.dto.PasswordResetRequestRequest;
import com.withquery.webide_spring_be.domain.auth.dto.PasswordResetRequestResponse;
import com.withquery.webide_spring_be.domain.auth.dto.PasswordResetConfirmRequest;
import com.withquery.webide_spring_be.domain.auth.dto.PasswordResetConfirmResponse;
import com.withquery.webide_spring_be.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
        summary = "로그인",
        description = "이메일과 비밀번호로 로그인합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                schema = @Schema(implementation = LoginResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (이메일 또는 비밀번호 오류)",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password-reset/request")
    @Operation(
        summary = "비밀번호 재설정 요청",
        description = "이메일로 비밀번호 재설정 토큰을 요청합니다. 토큰은 5분간 유효합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "비밀번호 재설정 토큰 발급 성공",
            content = @Content(
                schema = @Schema(implementation = PasswordResetRequestResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (존재하지 않는 이메일)",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<PasswordResetRequestResponse> requestPasswordReset(
            @RequestBody PasswordResetRequestRequest request) {
        PasswordResetRequestResponse response = authService.requestPasswordReset(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password-reset/confirm")
    @Operation(
        summary = "비밀번호 재설정 확인",
        description = "비밀번호 재설정 토큰과 새 비밀번호로 비밀번호를 변경합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "비밀번호 변경 성공",
            content = @Content(
                schema = @Schema(implementation = PasswordResetConfirmResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효하지 않거나 만료된 토큰)",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<PasswordResetConfirmResponse> confirmPasswordReset(
            @RequestBody PasswordResetConfirmRequest request) {
        PasswordResetConfirmResponse response = authService.confirmPasswordReset(request);
        return ResponseEntity.ok(response);
    }
} 