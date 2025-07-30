package com.withquery.webide_spring_be.domain.collaboration.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withquery.webide_spring_be.common.dto.ErrorResponse;
import com.withquery.webide_spring_be.domain.collaboration.dto.InvitationActionRequest;
import com.withquery.webide_spring_be.domain.collaboration.dto.InvitationResponse;
import com.withquery.webide_spring_be.domain.collaboration.dto.InviteMemberRequest;
import com.withquery.webide_spring_be.domain.collaboration.dto.ProjectMemberResponse;
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

	@PostMapping("/{projectId}/members/invite")
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
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "권한 없음 (소유자가 아닌 경우)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "프로젝트 또는 사용자를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
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

	@GetMapping("/{projectId}/members")
	@Operation(
		summary = "프로젝트 멤버 목록 조회",
		description = "프로젝트의 모든 활성 멤버 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "멤버 목록 조회 성공",
			content = @Content(
				schema = @Schema(implementation = ProjectMemberResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "권한 없음 (프로젝트 멤버가 아닌 경우)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "프로젝트를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(
		@Parameter(description = "프로젝트 ID", required = true)
		@PathVariable Long projectId,
		Authentication authentication) {
		String userEmail = authentication.getName();

		List<ProjectMemberResponse> members = projectMemberService.getProjectMembers(projectId, userEmail);
		return ResponseEntity.ok(members);
	}

	@GetMapping("/invitations")
	@Operation(
		summary = "초대 수신 목록 조회",
		description = "현재 사용자가 받은 모든 프로젝트 초대 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "초대 목록 조회 성공",
			content = @Content(
				schema = @Schema(implementation = InvitationResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "사용자를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<List<InvitationResponse>> getUserInvitations(
		Authentication authentication) {
		String userEmail = authentication.getName();

		List<InvitationResponse> invitations = projectMemberService.getUserInvitations(userEmail);
		return ResponseEntity.ok(invitations);
	}

	@GetMapping("/invitations/sent")
	@Operation(
		summary = "초대 발신 목록 조회",
		description = "현재 사용자가 보낸 모든 프로젝트 초대 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "보낸 초대 목록 조회 성공",
			content = @Content(
				schema = @Schema(implementation = InvitationResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "사용자를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<List<InvitationResponse>> getSentInvitations(
		Authentication authentication) {
		String userEmail = authentication.getName();

		List<InvitationResponse> sentInvitations = projectMemberService.getSentInvitations(userEmail);
		return ResponseEntity.ok(sentInvitations);
	}

	@PutMapping("/invitations/{invitationId}")
	@Operation(
		summary = "초대 응답 처리",
		description = "프로젝트 초대를 수락하거나 거절합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "초대 응답 처리 성공"
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (이미 처리된 초대)",
			content = @Content(schema = @Schema(implementation = String.class))
		),
		@ApiResponse(
			responseCode = "403",
			description = "권한 없음 (초대받은 사용자가 아닌 경우)",
			content = @Content(schema = @Schema(implementation = String.class))
		),
		@ApiResponse(
			responseCode = "404",
			description = "초대를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = String.class))
		)
	})
	public ResponseEntity<Void> handleInvitation(
		@Parameter(description = "초대 ID", required = true)
		@PathVariable Long invitationId,
		@Parameter(description = "초대 응답 액션", required = true)
		@Valid @RequestBody InvitationActionRequest request,
		Authentication authentication) {
		String userEmail = authentication.getName();

		projectMemberService.handleInvitation(invitationId, userEmail, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{projectId}/members/leave")
	@Operation(
		summary = "프로젝트 탈퇴",
		description = "현재 사용자가 프로젝트에서 탈퇴합니다. 소유자는 탈퇴할 수 없습니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "프로젝트 탈퇴 성공"
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (소유자는 탈퇴 불가, 최소 멤버 수 유지 필요)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "권한 없음 (프로젝트 멤버가 아닌 경우)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "프로젝트를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<Void> leaveProject(
		@Parameter(description = "프로젝트 ID", required = true)
		@PathVariable Long projectId,
		Authentication authentication) {
		String userEmail = authentication.getName();

		projectMemberService.leaveProject(projectId, userEmail);
		return ResponseEntity.ok().build();
	}

}
