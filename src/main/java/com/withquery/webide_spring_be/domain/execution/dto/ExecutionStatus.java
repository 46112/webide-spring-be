package com.withquery.webide_spring_be.domain.execution.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코드 실행 상태 유형")
public enum ExecutionStatus {
	SUCCESS,
	RUNTIME_ERROR,
	TIMEOUT,
	MEMORY_LIMIT,
	COMPILE_ERROR,
	SYSTEM_ERROR
}