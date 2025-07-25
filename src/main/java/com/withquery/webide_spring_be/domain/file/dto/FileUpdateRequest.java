package com.withquery.webide_spring_be.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일/디렉토리 수정 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUpdateRequest {
	@Schema(description = "파일 고유 ID")
	@NotNull(message = "파일 ID는 필수입니다.")
	private Long id;

	@Schema(description = "새 이름")
	@NotBlank(message = "새 이름은 필수입니다.")
	@Size(max = 255, message = "파일/디렉토리 이름은 255자를 초과할 수 없습니다.")
	private String newName;

	@Schema(description = "새 상위 디렉토리 ID")
	private Long newParentId;
}