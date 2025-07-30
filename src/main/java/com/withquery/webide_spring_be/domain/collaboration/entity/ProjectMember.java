package com.withquery.webide_spring_be.domain.collaboration.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_members",
	uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "프로젝트 멤버 엔티티")
public class ProjectMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "멤버 고유 ID", example = "1")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	@Schema(description = "소속 프로젝트")
	private Project project;

	@Column(name = "user_id", nullable = false)
	@Schema(description = "사용자 ID", example = "123")
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Schema(description = "멤버 역할", example = "MEMBER", implementation = ProjectMemberRole.class)
	private ProjectMemberRole role;

	@CreationTimestamp
	@Column(name = "joined_at", nullable = false, updatable = false)
	@Schema(description = "프로젝트 참여 일시", example = "2025-07-25T10:30:00")
	private LocalDateTime joinedAt;

	@Schema(hidden = true)
	public boolean isOwner() {
		return this.role == ProjectMemberRole.OWNER;
	}

	@Schema(hidden = true)
	public boolean isMember() {
		return this.role == ProjectMemberRole.MEMBER;
	}

	@Schema(hidden = true)
	@Builder.Default
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

}
