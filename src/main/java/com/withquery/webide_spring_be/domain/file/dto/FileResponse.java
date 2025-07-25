package com.withquery.webide_spring_be.domain.file.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일/디렉토리 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
	@Schema(description = "파일 고유 ID")
	private Long id;

	@Schema(description = "파일/디렉토리 이름")
	private String name;

	@Schema(description = "타입")
	private String type;

	@Schema(description = "상위 디렉토리 ID")
	private Long parentId;

	@Schema(description = "소속 프로젝트 ID")
	private Long projectId;

	@Schema(description = "파일/디렉토리 전체 경로")
	private String path;

	@Schema(description = "파일 생성 일시")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@Schema(description = "파일 수정 일시")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
}