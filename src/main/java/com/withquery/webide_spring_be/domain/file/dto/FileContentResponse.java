package com.withquery.webide_spring_be.domain.file.dto;

import com.withquery.webide_spring_be.domain.file.entity.File;

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
	@Schema(description = "파일 고유 ID", example = "123")
	private Long fileId;

	@Schema(description = "파일 내용", example = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}")
	private String content;

	public static FileContentResponse from(File file) {
		return new FileContentResponse(file.getId(), file.getContent());
	}
}