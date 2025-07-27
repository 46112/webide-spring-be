package com.withquery.webide_spring_be.domain.project.dto;

import java.util.List;

import com.withquery.webide_spring_be.domain.file.dto.FileTreeNode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "프로젝트 상세 정보 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetailResponse {
	@Schema(description = "프로젝트 정보")
	private ProjectResponse project;

	@Schema(description = "파일 트리")
	private List<FileTreeNode> fileTree;

	@Schema(description = "응답 메시지", example = "프로젝트 상세 정보가 성공적으로 조회되었습니다.")
	private String message;

	public static ProjectDetailResponse from(ProjectResponse project, FileTreeNode fileTree) {
		return ProjectDetailResponse.builder()
			.project(project)
			.fileTree(List.of(fileTree))
			.build();
	}
}