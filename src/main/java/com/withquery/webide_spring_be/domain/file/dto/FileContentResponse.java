package com.withquery.webide_spring_be.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일 내용 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileContentResponse {
	@Schema(description = "파일 고유 ID")
	private Long fileId;

	@Schema(description = "파일 내용")
	private String content;
}