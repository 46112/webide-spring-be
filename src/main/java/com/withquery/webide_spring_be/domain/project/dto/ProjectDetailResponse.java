package com.withquery.webide_spring_be.domain.project.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "프로젝트 상세 정보")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailResponse {
	@Schema(description = "프로젝트 정보")
	private ProjectResponse project;

	@Schema(description = "파일 트리")
	private List<ProjectFileTreeNode> fileTree;
}