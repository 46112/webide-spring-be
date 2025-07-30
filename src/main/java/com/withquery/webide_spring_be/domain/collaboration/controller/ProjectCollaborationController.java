package com.withquery.webide_spring_be.domain.collaboration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withquery.webide_spring_be.common.dto.ErrorResponse;
import com.withquery.webide_spring_be.domain.collaboration.dto.InviteMemberRequest;
import com.withquery.webide_spring_be.domain.collaboration.service.ProjectMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Collaboration", description = "프로젝트 협업 관리 API")
@RequiredArgsConstructor
public class ProjectCollaborationController {

	private final ProjectMemberService projectMemberService;

	@Operation(
		summary = "프로젝트 멤버 초대",
		description = "프로젝트 소유자가 다른 사용자를 프로젝트에 초대합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "초대 성공"
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (이미 멤버이거나 이미 초대된 경우)",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))
		),
		@ApiResponse(
			responseCode = "403",
			description = "권한 없음 (소유자가 아닌 경우)",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))
		),
		@ApiResponse(
			responseCode = "404",
			description = "프로젝트 또는 사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))
		)
	})
	@PostMapping("/{projectId}/members/invite")
	public ResponseEntity<Void> inviteMember(
		@Parameter(description = "프로젝트 ID", required = true)
		@PathVariable Long projectId,
		@Parameter(description = "초대할 멤버 정보", required = true)
		@Valid @RequestBody InviteMemberRequest request,
		Authentication authentication) {

		String userEmail = authentication.getName();
		projectMemberService.inviteMember(projectId, userEmail, request);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
