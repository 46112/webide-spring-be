package com.withquery.webide_spring_be.domain.project.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파일 타입")
public enum FileType {

	@Schema(description = "파일")
	FILE("파일"),

	@Schema(description = "디렉토리")
	DIRECTORY("디렉토리");

	@Schema(description = "타입 설명")
	private final String description;

	FileType(String description) {
		this.description = description;
	}

	@Schema(description = "파일 타입 설명", example = "파일")
	public String getDescription() {
		return description;
	}

}
