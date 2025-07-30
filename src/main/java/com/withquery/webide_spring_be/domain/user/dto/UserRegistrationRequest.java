package com.withquery.webide_spring_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
<<<<<<< HEAD
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public record UserRegistrationRequest(
	@Schema(description = "이메일", example = "user@example.com", required = true)
	String email,

	@Schema(description = "닉네임", example = "username", required = true)
	String nickname,

	@Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?`~]).{10,}$",
		message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 각각 1자 이상 포함해야 합니다."
	)

	@Schema(description = "비밀번호", example = "Password123!", required = true)
	String password
) {
}
=======
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "회원가입 요청")
public class UserRegistrationRequest {

    @Schema(description = "이메일", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "닉네임", example = "username", required = true)
    private String nickname;

    @Schema(description = "비밀번호", example = "password123", required = true)
    private String password;
} 
>>>>>>> d05f6a04281ebed51345f61c49474a437f48c11a
