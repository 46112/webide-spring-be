package com.withquery.webide_spring_be.domain.execution.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withquery.webide_spring_be.common.dto.ErrorResponse;
import com.withquery.webide_spring_be.domain.collaboration.entity.ProjectMemberRole;
import com.withquery.webide_spring_be.domain.collaboration.service.ProjectMemberService;
import com.withquery.webide_spring_be.domain.execution.dto.ExecutionRequest;
import com.withquery.webide_spring_be.domain.execution.dto.ExecutionResult;
import com.withquery.webide_spring_be.domain.execution.service.ExecutionService;
import com.withquery.webide_spring_be.util.jwt.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectId}/execution")
@RequiredArgsConstructor
@Tag(name = "Execution", description = "코드 실행 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class ExecutionController {

	private final ExecutionService executionService;
	private final ProjectMemberService projectMemberService;

	@PostMapping("/files/{fileId}/run")
	@Operation(
		summary = "파일 코드 실행",
		description = "특정 파일의 코드를 실행하고 결과를 반환합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "코드 실행 성공",
			content = @Content(
				schema = @Schema(implementation = ExecutionResult.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (지원하지 않는 언어, 빈 파일 등)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "권한 없음 (멤버 이상만 가능)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "파일을 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "408",
			description = "실행 시간 초과",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "500",
			description = "실행 환경 오류",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<ExecutionResult> executeFile(
		@PathVariable Long projectId,
		@PathVariable Long fileId,
		@RequestBody ExecutionRequest request,
		Authentication authentication) {

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String userEmail = userDetails.getEmail();

		if (!projectMemberService.hasPermission(projectId, userEmail,
			ProjectMemberRole.MEMBER, ProjectMemberRole.OWNER)) {
			throw new RuntimeException("코드를 실행할 권한이 없습니다.");
		}

		ExecutionResult result = executionService.executeFile(projectId, fileId, request);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/code/run")
	@Operation(
		summary = "임시 코드 실행",
		description = "파일에 저장되지 않은 임시 코드를 실행합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "코드 실행 성공",
			content = @Content(
				schema = @Schema(implementation = ExecutionResult.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "권한 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<ExecutionResult> executeCode(
		@PathVariable Long projectId,
		@RequestBody ExecutionRequest request,
		Authentication authentication) {

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String userEmail = userDetails.getEmail();

		if (!projectMemberService.hasPermission(projectId, userEmail,
			ProjectMemberRole.MEMBER, ProjectMemberRole.OWNER)) {
			throw new RuntimeException("코드를 실행할 권한이 없습니다.");
		}

		if (request.getLanguage() == null || request.getLanguage().isBlank()) {
			throw new IllegalArgumentException("언어를 지정해야 합니다.");
		}

		if (request.getCode() == null || request.getCode().isBlank()) {
			throw new IllegalArgumentException("실행할 코드를 입력해야 합니다.");
		}

		ExecutionResult result = executionService.executeCode(projectId, request);
		return ResponseEntity.ok(result);
	}
}
