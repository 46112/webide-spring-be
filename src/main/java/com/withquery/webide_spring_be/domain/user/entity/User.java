package com.withquery.webide_spring_be.domain.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "사용자 엔티티")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "사용자 ID", example = "1")
	private Long id;

	@Column(nullable = false, unique = true)
	@Schema(description = "이메일", example = "user@example.com")
	private String email;

	@Column(nullable = false, unique = true)
	@Schema(description = "닉네임", example = "username")
	private String nickname;

	@Column(nullable = false)
	@Schema(description = "비밀번호", example = "hashedPassword")
	private String password;

	@Column(name = "created_at")
	@Schema(description = "생성 시간", example = "2025-07-20T11:38:52.88913651")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@Schema(description = "수정 시간", example = "2025-07-20T11:38:52.889143593")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
} 