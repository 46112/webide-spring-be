package com.withquery.webide_spring_be.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일 내용 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileContentUpdateRequest {
	@Schema(description = "파일 고유 ID", example = "123")
	@NotNull(message = "파일 ID는 필수입니다")
	private Long fileId;

	@Schema(description = "파일 내용", example = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello new World\");\n    }\n}")
	private String content;
}