package com.withquery.webide_spring_be.domain.execution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "코드 실행 요청")
public class ExecutionRequest {

	@Schema(
		description = "실행할 코드 내용",
		example = "console.log('Hello, World!');"
	)
	private String code;

	@Schema(
		description = "프로그래밍 언어",
		example = "javascript",
		allowableValues = {"javascript", "python", "java", "cpp", "c", "go", "rust", "sql", "mysql", "postgresql",
			"mongodb"}
	)
	private String language;

	@Schema(
		description = "코드 실행 시 표준 입력으로 전달할 데이터",
		example = "5\n10"
	)
	private String input;

	@Schema(description = "코드 실행 옵션 설정")
	private ExecutionOptions options;

	@Data
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "코드 실행 시 적용할 옵션들")
	public static class ExecutionOptions {
		@Schema(
			description = "실행 제한 시간 (초)",
			example = "10",
			minimum = "1",
			maximum = "60"
		)
		private Integer timeoutSeconds = 10;

		@Schema(
			description = "메모리 사용 제한 (MB)",
			example = "128",
			minimum = "32",
			maximum = "512"
		)
		private Integer memoryLimitMB = 128;

		@Schema(
			description = "언어 버전 (선택사항)",
			example = "18.17.0",
			examples = {
				"Node.js: 18.17.0, 20.5.0",
				"Python: 3.9, 3.10, 3.11",
				"Java: 11, 17, 21"
			}
		)
		private String version;
	}
}