package com.withquery.webide_spring_be.domain.collaboration.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import com.withquery.webide_spring_be.domain.project.entity.Project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_invitations")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "프로젝트 멤버 초대 엔티티")
public class ProjectInvitation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "멤버 고유 ID", example = "1")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	@Schema(description = "소속 프로젝트")
	private Project project;

	@Column(name = "inviter_id", nullable = false)
	@Schema(description = "초대자 ID", example = "123")
	private Long inviterId;

	@Column(name = "invitee_id", nullable = false)
	@Schema(description = "피초대자 ID", example = "456")
	private Long inviteeId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Schema(description = "멤버 역할", example = "MEMBER", implementation = ProjectMemberRole.class)
	private ProjectMemberRole role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Schema(description = "초대 상태", example = "ACCEPTED", implementation = InvitationStatus.class)
	private InvitationStatus status;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	@Schema(description = "프로젝트 초대 생성 일시", example = "2025-07-25T10:30:00")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	@Schema(description = "프로젝트 초대 수정 일시", example = "2025-07-26T10:30:00")
	private LocalDateTime updatedAt;

	@Schema(description = "초대를 수락합니다.")
	public void accept() {
		this.status = InvitationStatus.ACCEPTED;
		this.updatedAt = LocalDateTime.now();
	}

	@Schema(description = "초대를 거절합니다.")
	public void reject() {
		this.status = InvitationStatus.REJECTED;
		this.updatedAt = LocalDateTime.now();
	}

	@Schema(description = "초대 대기 상태 여부를 확인합니다.")
	public boolean isPending() {
		return this.status == InvitationStatus.PENDING;
	}
}
