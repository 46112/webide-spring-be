package com.withquery.webide_spring_be.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일 내용 저장 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileContentSaveRequest {
	@Schema(description = "저장할 파일의 ID", example = "1")
	@NotNull(message = "파일 ID는 필수입니다")
	private Long fileId;

	@Schema(description = "파일 내용 (디렉토리인 경우 null)", example = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}")
	private String content;

	@Schema(description = "파일 인코딩", example = "UTF-8", defaultValue = "UTF-8")
	@Builder.Default
	private String encoding = "UTF-8";

	@Schema(description = "자동 저장 여부 (true: 자동 저장, false: 수동 저장)", example = "false", defaultValue = "false")
	@Builder.Default
	private boolean autoSave = false;
}