package com.withquery.webide_spring_be.domain.file.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.withquery.webide_spring_be.common.dto.ErrorResponse;
import com.withquery.webide_spring_be.domain.file.dto.FileContentResponse;
import com.withquery.webide_spring_be.domain.file.dto.FileContentSaveRequest;
import com.withquery.webide_spring_be.domain.file.dto.FileCreateRequest;
import com.withquery.webide_spring_be.domain.file.dto.FileResponse;
import com.withquery.webide_spring_be.domain.file.dto.FileTreeNode;
import com.withquery.webide_spring_be.domain.file.dto.FileUpdateRequest;
import com.withquery.webide_spring_be.domain.file.service.FileService;
import com.withquery.webide_spring_be.domain.project.entity.ProjectMemberRole;
import com.withquery.webide_spring_be.domain.project.service.ProjectMemberService;
import com.withquery.webide_spring_be.domain.user.entity.User;
import com.withquery.webide_spring_be.util.jwt.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectId}/")
@RequiredArgsConstructor
@Tag(name = "File", description = "프로젝트 파일/디렉토리 관리 API")
public class FileController {

	private final FileService fileService;
	private final ProjectMemberService projectMemberService;

	@PostMapping("/root-directory")
	@Operation(
		summary = "루트 디렉토리 생성",
		description = "프로젝트 생성 시 해당 프로젝트의 루트 디렉토리를 생성합니다.."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "루트 디렉토리 생성 성공"
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (프로젝트 이름 누락)",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "권한 없음 (프로젝트 소유자만 가능)",
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
	public ResponseEntity<Void> createRootDirectory(
		@PathVariable Long projectId,
		@RequestParam @NotBlank String projectName,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();
		projectMemberService.hasPermission(projectId, userEmail, ProjectMemberRole.OWNER);
		fileService.createRootDirectory(projectId, projectName);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	@Operation(
		summary = "파일 트리 조회",
		description = "특정 프로젝트의 파일 및 디렉토리 구조를 트리 형태로 조회합니다. 프로젝트 멤버만 접근 가능합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "파일 트리 조회 성공",
			content = @Content(
				schema = @Schema(implementation = FileTreeNode.class)
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
	public ResponseEntity<FileTreeNode> getFileTree(
		@PathVariable Long projectId,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();

		User currentUser = projectMemberService.getUserByEmail(userEmail);
		if (!projectMemberService.hasPermission(projectId, currentUser.getEmail(), ProjectMemberRole.MEMBER,
			ProjectMemberRole.OWNER)) {
			throw new RuntimeException("프로젝트 파일 트리를 조회할 권한이 없습니다.");
		}

		FileTreeNode fileTree = fileService.getFileTree(projectId);
		return ResponseEntity.ok(fileTree);
	}

	@PostMapping("/files")
	@Operation(
		summary = "파일/디렉토리 생성",
		description = "특정 프로젝트 내에 새로운 파일 또는 디렉토리를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "파일/디렉토리 생성 성공",
			content = @Content(
				schema = @Schema(implementation = FileResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (이름, 타입 누락 등)",
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
			description = "프로젝트 또는 부모 디렉토리를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "409",
			description = "파일/디렉토리 이름이 이미 존재함",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<FileResponse> createFile(
		@PathVariable Long projectId,
		@Valid @RequestBody FileCreateRequest request,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();

		User currentUser = projectMemberService.getUserByEmail(userEmail);
		if (!projectMemberService.hasPermission(projectId, currentUser.getEmail(), ProjectMemberRole.MEMBER,
			ProjectMemberRole.OWNER)) {
			throw new RuntimeException("파일/디렉토리를 생성할 권한이 없습니다.");
		}

		FileResponse response = fileService.createFile(projectId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{fileId}")
	@Operation(
		summary = "파일/디렉토리 수정",
		description = "특정 파일 또는 디렉토리의 이름이나 경로를 수정합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "파일/디렉토리 수정 성공",
			content = @Content(
				schema = @Schema(implementation = FileResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (새 이름 누락 등)",
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
			description = "파일/디렉토리 또는 새 경로를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "409",
			description = "새로운 경로에 이미 파일/디렉토리 이름이 존재함",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<FileResponse> updateFile(
		@PathVariable Long projectId,
		@PathVariable Long fileId,
		@Valid @RequestBody FileUpdateRequest request,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();

		User currentUser = projectMemberService.getUserByEmail(userEmail);
		if (!projectMemberService.hasPermission(projectId, currentUser.getEmail(), ProjectMemberRole.MEMBER,
			ProjectMemberRole.OWNER)) {
			throw new RuntimeException("파일/디렉토리를 수정할 권한이 없습니다.");
		}

		request.setId(fileId);

		FileResponse response = fileService.updateFile(projectId, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{fileId}")
	@Operation(
		summary = "파일/디렉토리 삭제",
		description = "특정 파일 또는 디렉토리를 삭제합니다. 루트 디렉토리는 삭제할 수 없습니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "파일/디렉토리 삭제 성공"
		),
		@ApiResponse(
			responseCode = "400",
			description = "루트 디렉토리는 삭제할 수 없음",
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
			description = "파일/디렉토리를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	public ResponseEntity<Void> deleteFile(
		@PathVariable Long projectId,
		@PathVariable Long fileId,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();

		User currentUser = projectMemberService.getUserByEmail(userEmail);
		if (!projectMemberService.hasPermission(projectId, currentUser.getEmail(), ProjectMemberRole.MEMBER,
			ProjectMemberRole.OWNER)) {
			throw new RuntimeException("파일/디렉토리를 삭제할 권한이 없습니다.");
		}

		fileService.deleteFile(projectId, fileId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{fileId}/content")
	@Operation(
		summary = "파일 내용 조회",
		description = "특정 파일의 내용을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "파일 내용 조회 성공",
			content = @Content(
				schema = @Schema(implementation = FileContentResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "파일 유형이 아님 (디렉토리인 경우)",
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
		)
	})
	public ResponseEntity<FileContentResponse> getFileContent(
		@PathVariable Long projectId,
		@PathVariable Long fileId,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();

		User currentUser = projectMemberService.getUserByEmail(userEmail);
		if (!projectMemberService.hasPermission(projectId, currentUser.getEmail(), ProjectMemberRole.MEMBER,
			ProjectMemberRole.OWNER)) {
			throw new RuntimeException("파일 내용을 조회할 권한이 없습니다.");
		}

		FileContentResponse response = fileService.getFileContent(projectId, fileId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{fileId}/content")
	@Operation(
		summary = "파일 내용 저장",
		description = "특정 파일의 내용을 저장(업데이트)합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "파일 내용 저장 성공"
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 (파일 유형이 아님)",
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
		)
	})
	public ResponseEntity<Void> saveFileContent(
		@PathVariable Long projectId,
		@PathVariable Long fileId,
		@Valid @RequestBody FileContentSaveRequest request,
		Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userEmail = userDetails.getEmail();

		User currentUser = projectMemberService.getUserByEmail(userEmail);
		if (!projectMemberService.hasPermission(projectId, currentUser.getEmail(), ProjectMemberRole.MEMBER,
			ProjectMemberRole.OWNER)) {
			throw new RuntimeException("파일 내용을 저장할 권한이 없습니다.");
		}

		request.setFileId(fileId);

		fileService.saveFileContent(projectId, request);
		return ResponseEntity.ok().build();
	}
}
