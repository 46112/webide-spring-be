package com.withquery.webide_spring_be.domain.collaboration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "프로젝트 멤버 초대 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteMemberRequest {
	@Schema(description = "초대할 사용자의 이메일", example = "user@example.com")
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "올바른 이메일 형식이어야 합니다.")
	private String email;
}
