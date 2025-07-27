package com.withquery.webide_spring_be.domain.project.exception;

public abstract class ProjectException extends RuntimeException {
	public ProjectException(String message) {
		super(message);
	}

	public ProjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public abstract String getErrorCode();
}
