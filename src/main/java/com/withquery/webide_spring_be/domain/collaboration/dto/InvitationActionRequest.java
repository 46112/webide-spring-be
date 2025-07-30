package com.withquery.webide_spring_be.domain.collaboration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "프로젝트 초대 처리 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationActionRequest {
	@Schema(description = "초대에 대한 처리 액션", example = "ACCEPT")
	@NotBlank(message = "액션은 필수입니다.")
	private InvitationAction action;
}
