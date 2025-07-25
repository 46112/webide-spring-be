package com.withquery.webide_spring_be.domain.project.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "프로젝트 목록")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListResponse {
	@Schema(description = "프로젝트 목록")
	private List<ProjectResponse> projects;
}