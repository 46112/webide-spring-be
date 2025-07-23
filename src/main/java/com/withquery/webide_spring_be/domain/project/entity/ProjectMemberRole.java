package com.withquery.webide_spring_be.domain.project.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로젝트 멤버 역할 열거형")
public enum ProjectMemberRole {
	@Schema(description = "프로젝트 소유자")
	OWNER("소유자"),

	@Schema(description = "소속 멤버")
	MEMBER("멤버");

	@Schema(description = "역할 설명")
	private final String description;

	ProjectMemberRole(String description) {
		this.description = description;
	}

	@Schema(description = "멤버 역할 설명", example = "소유자")
	public String getDescription() {
		return description;
	}
}