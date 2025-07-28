package com.withquery.webide_spring_be.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "프로젝트 수정 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequest {
	@Schema(description = "프로젝트 새 이름", example = "My New Project")
	@NotBlank(message = "프로젝트 이름은 필수값입니다.")
	@Size(max = 100, message = "프로젝트 이름은 100자를 초과할 수 없습니다.")
	private String newName;

	@Schema(description = "프로젝트 새 설명", example = "SQL 프로젝트입니다.")
	@Size(max = 500, message = "프로젝트 설명은 500자를 초과할 수 없습니다.")
	private String newDescription;
}
