package com.withquery.webide_spring_be.domain.collaboration.dto;

import java.time.LocalDateTime;

import com.withquery.webide_spring_be.domain.collaboration.entity.InvitationStatus;
import com.withquery.webide_spring_be.domain.collaboration.entity.ProjectMemberRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "프로젝트 멤버 응답")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberResponse {
	@Schema(description = "사용자 고유 ID", example = "123")
	private Long id;

	@Schema(description = "사용자 닉네임", example = "user")
	private String userName;

	@Schema(description = "사용자 이메일", example = "use@exmaple.com")
	private String userEmail;

	@Schema(description = "사용자의 역할", example = "MEMBER")
	private ProjectMemberRole role;

	@Schema(description = "프로젝트 가입 일시", example = "2025-07-26T10:30:00")
	private LocalDateTime joinedAt;

	@Schema(description = "현재 프로젝트 활성 상태 여부", example = "true")
	private boolean isActive;


}
