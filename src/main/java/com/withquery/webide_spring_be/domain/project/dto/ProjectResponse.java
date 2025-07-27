package com.withquery.webide_spring_be.domain.project.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.withquery.webide_spring_be.domain.project.entity.Project;
import com.withquery.webide_spring_be.domain.project.entity.ProjectMemberRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "프로젝트 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
	@Schema(description = "프로젝트 고유 ID", example = "001")
	private Long id;

	@Schema(description = "멤버 역할", example = "MEMBER")
	private ProjectMemberRole role;

	@Schema(description = "프로젝트 이름", example = "My Project")
	private String name;

	@Schema(description = "프로젝트 설명", example = "데이터 분석을 위한 SQL 프로젝트입니다.")
	private String description;

	@Schema(description = "프로젝트 생성 일시", example = "2025-07-22 10:30:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@Schema(description = "프로젝트 수정 일시", example = "2025-07-23 12:30:30")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	@Schema(description = "응답 메시지", example = "프로젝트 정보가 성공적으로 조회되었습니다.")
	private String message;

	public static ProjectResponse from(Project project) {
		return ProjectResponse.builder()
			.id(project.getId())
			.name(project.getName())
			.description(project.getDescription())
			.createdAt(project.getCreatedAt())
			.updatedAt(project.getUpdatedAt())
			.build();
	}

	public static ProjectResponse from(Project project, String message) {
		return ProjectResponse.builder()
			.id(project.getId())
			.name(project.getName())
			.description(project.getDescription())
			.createdAt(project.getCreatedAt())
			.updatedAt(project.getUpdatedAt())
			.message(message)
			.build();
	}

	public static ProjectResponse from(Project project, ProjectMemberRole userRole) {
		return ProjectResponse.builder()
			.id(project.getId())
			.name(project.getName())
			.description(project.getDescription())
			.createdAt(project.getCreatedAt())
			.updatedAt(project.getUpdatedAt())
			.role(userRole)
			.build();
	}
}