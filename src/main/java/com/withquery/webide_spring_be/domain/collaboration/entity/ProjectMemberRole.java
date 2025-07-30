package com.withquery.webide_spring_be.domain.collaboration.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "프로젝트 멤버 역할 열거형")
@Getter
@RequiredArgsConstructor
public enum ProjectMemberRole {
	@Schema(description = "프로젝트 소유자")
	OWNER("소유자"),

	@Schema(description = "소속 멤버")
	MEMBER("멤버");

	@Schema(description = "역할 설명")
	private final String description;
}