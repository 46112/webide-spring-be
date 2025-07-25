package com.withquery.webide_spring_be.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "프로젝트 생성 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {
	@Schema(description = "프로젝트 이름", example = "My Project")
	@NotBlank(message = "프로젝트 이름을 입력하세요.")
	@Size(max = 100, message = "프로젝트 이름은 100자를 초과할 수 없습니다.")
	private String name;

	@Schema(description = "프로젝트 설명", example = "프로젝트 상세 설명")
	@Size(max = 500, message = "프로젝트 설명은 500자를 초과할 수 없습니다.")
	private String description;
}
