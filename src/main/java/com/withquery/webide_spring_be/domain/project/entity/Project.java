package com.withquery.webide_spring_be.domain.project.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "프로젝트 엔티티")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "파일 고유 ID", example = "1")
	private Long id;

}
