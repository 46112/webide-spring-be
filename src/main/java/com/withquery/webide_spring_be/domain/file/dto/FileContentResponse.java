package com.withquery.webide_spring_be.domain.file.dto;

import com.withquery.webide_spring_be.domain.file.entity.File;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "파일 내용 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileContentResponse {
	@Schema(description = "파일 고유 ID", example = "123")
	private Long fileId;

	@Schema(description = "파일 이름", example = "main.java")
	private String fileName;

	@Schema(description = "파일 내용", example = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}")
	private String content;

	@Schema(description = "응답 메시지", example = "파일 내용을 성공적으로 조회되었습니다.")
	private String message;

	public static FileContentResponse from(File file) {

		return FileContentResponse.builder()
			.fileId(file.getId())
			.content(file.getContent())
			.build();
	}

	public static FileContentResponse from(File file, String message) {

		return FileContentResponse.builder()
			.fileId(file.getId())
			.fileName(file.getName())
			.content(file.getContent())
			.message(message)
			.build();
	}
}