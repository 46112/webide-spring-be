package com.withquery.webide_spring_be.domain.collaboration.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withquery.webide_spring_be.domain.collaboration.dto.InvitationActionRequest;
import com.withquery.webide_spring_be.domain.collaboration.dto.InvitationResponse;
import com.withquery.webide_spring_be.domain.collaboration.dto.InviteMemberRequest;
import com.withquery.webide_spring_be.domain.collaboration.dto.ProjectMemberResponse;
import com.withquery.webide_spring_be.domain.collaboration.entity.InvitationStatus;
import com.withquery.webide_spring_be.domain.collaboration.entity.ProjectInvitation;
import com.withquery.webide_spring_be.domain.collaboration.repository.ProjectInvitationRepository;
import com.withquery.webide_spring_be.domain.project.entity.Project;
import com.withquery.webide_spring_be.domain.collaboration.entity.ProjectMember;
import com.withquery.webide_spring_be.domain.collaboration.entity.ProjectMemberRole;
import com.withquery.webide_spring_be.domain.collaboration.repository.ProjectMemberRepository;
import com.withquery.webide_spring_be.domain.project.repository.ProjectRepository;
import com.withquery.webide_spring_be.domain.user.entity.User;
import com.withquery.webide_spring_be.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final UserRepository userRepository;
	private final ProjectInvitationRepository projectInvitationRepository;

	@Override
	public User getUserByEmail(String userEmail) {
		return userRepository.findByEmail(userEmail)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
	}

	@Override
	public void addOwnerMember(Long projectId, String userEmail) {
		User user = getUserByEmail(userEmail);
		Long userId = user.getId();

		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

		ProjectMember ownerMember = ProjectMember.builder()
			.project(project)
			.userId(userId)
			.role(ProjectMemberRole.OWNER)
			.isActive(true)
			.build();

		projectMemberRepository.save(ownerMember);
	}

	@Override
	public Optional<ProjectMemberRole> getMemberRole(Long projectId, String userEmail) {
		User user = getUserByEmail(userEmail);
		Long userId = user.getId();

		return projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
			.map(ProjectMember::getRole);
	}

	@Override
	public boolean isMember(Long projectId, String userEmail) {
		User user = getUserByEmail(userEmail);
		Long userId = user.getId();

		return projectMemberRepository.existsByProjectIdAndUserIdAndIsActiveTrue(projectId, userId);
	}

	@Override
	public boolean hasPermission(Long projectId, String userEmail, ProjectMemberRole... requiredRoles) {
		if (requiredRoles.length == 0) {
			return isMember(projectId, userEmail);
		}

		Optional<ProjectMemberRole> memberRole = getMemberRole(projectId, userEmail);

		if (memberRole.isEmpty()) {
			return false;
		}

		return Arrays.stream(requiredRoles)
			.anyMatch(role -> memberRole.get() == role);
	}

	@Override
	public void inviteMember(Long projectId, String inviterEmail, InviteMemberRequest request) {
		if (!hasPermission(projectId, inviterEmail, ProjectMemberRole.OWNER)) {
			throw new IllegalArgumentException("프로젝트 멤버를 초대할 권한이 없습니다.");
		}

		User inviter = getUserByEmail(inviterEmail);
		User invitee = getUserByEmail(request.getEmail());

		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

		if (isMember(projectId, request.getEmail())) {
			throw new IllegalArgumentException("이미 프로젝트 멤버입니다.");
		}

		if (projectInvitationRepository.existsByProjectIdAndInviteeIdAndStatus(projectId, invitee.getId(),
			InvitationStatus.PENDING)) {
			throw new IllegalArgumentException("이미 초대를 보냈습니다.");
		}

		ProjectInvitation invitation = ProjectInvitation.builder()
			.project(project)
			.inviterId(inviter.getId())
			.inviteeId(invitee.getId())
			.status(InvitationStatus.PENDING)
			.build();

		projectInvitationRepository.save(invitation);
	}

	@Override
	public List<ProjectMemberResponse> getProjectMembers(Long projectId, String userEmail) {
		return List.of();
	}

	@Override
	public List<InvitationResponse> getUserInvitations(String userEmail) {
		return List.of();
	}

	@Override
	public void handleInvitation(Long invitationId, String userEmail, InvitationActionRequest request) {

	}

	@Override
	public void leaveProject(Long projectId, String userEmail) {

	}
}
