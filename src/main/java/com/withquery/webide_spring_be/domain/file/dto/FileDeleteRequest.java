package com.withquery.webide_spring_be.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일/디렉토리 삭제 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDeleteRequest {
	@Schema(description = "파일 고유 ID")
	@NotNull(message = "파일 ID는 필수입니다.")
	private Long id;
}