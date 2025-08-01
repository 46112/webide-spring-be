package com.withquery.webide_spring_be.domain.execution.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "코드 실행 결과")
public class ExecutionResult {
	@Schema(
		description = "실행 ID (추적용 고유 식별자)",
		example = "exec_20240801_143052_abc123"
	)
	private String executionId;

	@Schema(description = "실행 상태")
	private ExecutionStatus status;

	@Schema(
		description = "표준 출력 결과",
		example = "Hello, World!\nExecution completed successfully."
	)
	private String stdout;

	@Schema(
		description = "표준 에러 출력",
		example = "Warning: Deprecated function used at line 5"
	)
	private String stderr;

	@Schema(
		description = "프로세스 종료 코드 (0: 정상, 1~: 에러)",
		example = "0"
	)
	private Integer exitCode;

	@Schema(
		description = "실행 소요 시간 (밀리초)",
		example = "1250"
	)
	private Long executionTimeMs;

	@Schema(
		description = "사용된 메모리 (MB)",
		example = "45"
	)
	private Integer memoryUsedMB;

	@Schema(
		description = "실행 시작 시간",
		example = "2024-08-01T14:30:52"
	)
	private LocalDateTime startTime;

	@Schema(
		description = "실행 종료 시간",
		example = "2024-08-01T14:30:53"
	)
	private LocalDateTime endTime;

	@Schema(
		description = "에러 발생 시 상세 메시지",
		example = "SyntaxError: Unexpected token '}' at line 10"
	)
	private String errorMessage;

	public static ExecutionResult from(
		String executionId,
		ProcessResult result,
		ExecutionStatus status,
		LocalDateTime startTime,
		LocalDateTime endTime,
		long executionTimeMs
	) {
		return new ExecutionResult(
			executionId,
			status,
			result.getStdout(),
			result.getStderr(),
			result.getExitCode(),
			executionTimeMs,
			null,
			startTime,
			endTime,
			null
		);
	}

}