package com.withquery.webide_spring_be.domain.execution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class ProcessResult {
	final String stdout;
	final String stderr;
	final int exitCode;
}
