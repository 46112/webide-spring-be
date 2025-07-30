package com.withquery.webide_spring_be.domain.collaboration.dto;

import java.time.LocalDateTime;

import com.withquery.webide_spring_be.domain.collaboration.entity.InvitationStatus;
import com.withquery.webide_spring_be.domain.collaboration.entity.ProjectMemberRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "프로젝트 멤버 초대 응답")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationResponse {
	@Schema(description = "초대 ID", example = "123")
	private Long id;

	@Schema(description = "초대한 프로젝트 ID", example = "001")
	private Long projectId;

	@Schema(description = "초대한 프로젝트 이름", example = "with-query")
	private String projectName;

	@Schema(description = "초대한 유저의 닉네임", example = "김쿼리")
	private String inviterName;

	@Schema(description = "초대한 유저의 이메일", example = "use@exmaple.com")
	private String inviterEmail;

	@Schema(description = "초대된 사용자의 역할", example = "MEMBER")
	private ProjectMemberRole role;

	@Schema(description = "초대 상태", example = "ACCEPTED")
	private InvitationStatus status;

	@Schema(description = "초대 일시", example = "2025-07-25T10:30:00")
	private LocalDateTime createdAt;


}
