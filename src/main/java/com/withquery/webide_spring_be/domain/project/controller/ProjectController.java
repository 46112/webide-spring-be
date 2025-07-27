package com.withquery.webide_spring_be.domain.project.controller;

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
import com.withquery.webide_spring_be.domain.project.dto.ProjectCreateRequest;
import com.withquery.webide_spring_be.domain.project.dto.ProjectDetailResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectUpdateRequest;
import com.withquery.webide_spring_be.domain.project.service.ProjectService;
import com.withquery.webide_spring_be.util.jwt.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "프로젝트 관리 API")
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping
	@Operation(
		summary = "프로젝트 생성",
		description = "새로운 프로젝트를 생성합니다. 생성자는 자동으로 프로젝트 소유자가 됩니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "프로젝트 생성 성공",
			content = @Content(
				schema = @Schema(implementation = ProjectResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (프로젝트 이름 누락 등)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "인증되지 않은 사용자",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<ProjectResponse> createProject(
		Authentication authentication,
		@Valid @RequestBody ProjectCreateRequest request) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();
		ProjectResponse response = projectService.createProject(request, userEmail);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	@Operation(
		summary = "내 프로젝트 목록 조회",
		description = "사용자가 참여하고 있는 모든 프로젝트 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "프로젝트 목록 조회 성공",
			content = @Content(
				schema = @Schema(implementation = ProjectResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "인증되지 않은 사용자",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<List<ProjectResponse>> getMyProjects(
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();
		List<ProjectResponse> response = projectService.getMyProjects(userEmail);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{projectId}")
	@Operation(
		summary = "프로젝트 상세 정보 조회",
		description = "특정 프로젝트의 상세 정보와 파일 트리를 조회합니다. 프로젝트 멤버만 접근 가능합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "프로젝트 상세 정보 조회 성공",
			content = @Content(
				schema = @Schema(implementation = ProjectDetailResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "프로젝트 접근 권한 없음",
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
	public ResponseEntity<ProjectDetailResponse> getProjectDetail(
		@PathVariable Long projectId,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();
		ProjectDetailResponse response = projectService.getProjectDetail(projectId, userEmail);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{projectId}")
	@Operation(
		summary = "프로젝트 정보 수정",
		description = "프로젝트의 이름과 설명을 수정합니다. OWNER 또는 MEMBER 권한이 필요합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "프로젝트 수정 성공",
			content = @Content(
				schema = @Schema(implementation = ProjectResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (프로젝트 이름이 공백 등)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "프로젝트 수정 권한 없음",
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
	public ResponseEntity<ProjectResponse> updateProject(
		@PathVariable Long projectId,
		@Valid @RequestBody ProjectUpdateRequest request,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();
		ProjectResponse response = projectService.updateProject(projectId, request, userEmail);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{projectId}")
	@Operation(
		summary = "프로젝트 삭제",
		description = "프로젝트를 삭제합니다. OWNER 권한이 필요하며, 관련된 모든 파일도 함께 삭제됩니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "프로젝트 삭제 성공"
		),
		@ApiResponse(
			responseCode = "403",
			description = "프로젝트 삭제 권한 없음 (OWNER 권한 필요)",
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
	public ResponseEntity<Void> deleteProject(
		@PathVariable Long projectId,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();
		projectService.deleteProject(projectId, userEmail);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{projectId}/exists")
	@Operation(
		summary = "프로젝트 존재 여부 확인",
		description = "특정 프로젝트가 존재하는지 확인합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "프로젝트 존재 여부 확인 성공",
			content = @Content(
				schema = @Schema(type = "boolean")
			)
		)
	})
	public ResponseEntity<Boolean> existsProject(@PathVariable Long projectId) {
		boolean exists = projectService.existsProject(projectId);
		return ResponseEntity.ok(exists);
	}
}