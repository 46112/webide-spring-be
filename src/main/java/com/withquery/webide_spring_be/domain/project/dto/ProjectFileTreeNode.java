package com.withquery.webide_spring_be.domain.project.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일 트리 노드")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFileTreeNode {
	@Schema(description = "파일/디렉토리 ID")
	private Long id;

	@Schema(description = "파일/디렉토리 이름")
	private String name;

	@Schema(description = "타입")
	private String type;

	@Schema(description = "파일 경로")
	private String path;

	@Schema(description = "하위 항목들")
	private List<ProjectFileTreeNode> children;

	@Schema(description = "생성일시")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@Schema(description = "수정일시")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
}