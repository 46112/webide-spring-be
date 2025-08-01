package com.withquery.webide_spring_be.domain.file.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.withquery.webide_spring_be.domain.file.entity.File;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일/디렉토리 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
	@Schema(description = "파일 고유 ID", example = "123")
	private Long id;

	@Schema(description = "파일/디렉토리 이름", example = "main.java")
	private String name;

	@Schema(description = "타입", example = "FILE")
	private String type;

	@Schema(description = "상위 디렉토리 ID", example = "1")
	private Long parentId;

	@Schema(description = "소속 프로젝트 ID", example = "001")
	private Long projectId;

	@Schema(description = "파일/디렉토리 전체 경로", example = "/src/myProject/main.java")
	@Size(max = 500, message = "파일/디렉토리 경로는 500자를 초과할 수 없습니다.")
	private String path;

	@Schema(description = "파일 생성 일시", example = "2025-07-22T11:00:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@Schema(description = "파일 수정 일시", example = "2025-07-23T17:32:30")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	@Schema(description = "응답 메시지", example = "파일이 성공적으로 조회되었습니다.")
	private String message;

	public static FileResponse from(File file) {
		return FileResponse.builder()
			.id(file.getId())
			.name(file.getName())
			.type(file.getType().name())
			.parentId(file.getParentId())
			.projectId(file.getProject().getId())
			.path(file.getPath())
			.createdAt(file.getCreatedAt())
			.updatedAt(file.getUpdatedAt())
			.build();
	}

	public static FileResponse from(File file, String message) {
		return FileResponse.builder()
			.id(file.getId())
			.name(file.getName())
			.type(file.getType().name())
			.parentId(file.getParentId())
			.projectId(file.getProject().getId())
			.path(file.getPath())
			.createdAt(file.getCreatedAt())
			.updatedAt(file.getUpdatedAt())
			.message(message)
			.build();
	}
}