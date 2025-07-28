package com.withquery.webide_spring_be.domain.file.dto;

import com.withquery.webide_spring_be.domain.file.entity.FileType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일/디렉토리 생성 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileCreateRequest {
	@Schema(description = "파일/디렉토리 이름", example = "main.java")
	@NotBlank(message = "파일/디렉토리 이름은 필수입니다.")
	@Size(max = 255, message = "파일/디렉토리 이름은 255자를 초과할 수 없습니다.")
	private String name;

	@Schema(description = "타입 (FILE or DIRECTORY)", example = "FILE")
	@NotNull(message = "타입은 필수입니다.")
	private FileType type;

	@Schema(description = "상위 디렉토리 ID", example = "1")
	private Long parentId;
}