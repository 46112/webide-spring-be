package com.withquery.webide_spring_be.domain.execution.service;

import com.withquery.webide_spring_be.domain.execution.dto.ExecutionRequest;
import com.withquery.webide_spring_be.domain.execution.dto.ExecutionResult;

public interface ExecutionService {

	ExecutionResult executeFile(Long projectId, Long fileId, ExecutionRequest request);

	ExecutionResult executeCode(Long projectId, ExecutionRequest request);
}
