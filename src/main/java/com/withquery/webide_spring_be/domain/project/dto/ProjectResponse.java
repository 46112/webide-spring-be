package com.withquery.webide_spring_be.domain.project.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@Schema(description = "프로젝트 고유 ID")
	private Long id;

	@Schema(description = "프로젝트 이름")
	private String name;

	@Schema(description = "프로젝트 설명")
	private String description;

	@Schema(description = "프로젝트 생성 일시")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@Schema(description = "프로젝트 수정 일시")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
}