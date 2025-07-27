package com.withquery.webide_spring_be.domain.file.dto;

import com.withquery.webide_spring_be.domain.file.entity.FileType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
	@NotBlank(message = "타입은 필수입니다.")
	@Pattern(regexp = "^(FILE|DIRECTORY)$", message = "타입은 FILE 또는 DIRECTORY만 가능합니다.")
	private FileType type;

	@Schema(description = "상위 디렉토리 ID", example = "1")
	private Long parentId;

	@Schema(description = "파일 내용 (type이 FILE일 때만)", example = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}")
	private String content;
}