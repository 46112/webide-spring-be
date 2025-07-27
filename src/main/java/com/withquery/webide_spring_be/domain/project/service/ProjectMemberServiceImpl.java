package com.withquery.webide_spring_be.domain.project.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withquery.webide_spring_be.domain.project.entity.Project;
import com.withquery.webide_spring_be.domain.project.entity.ProjectMember;
import com.withquery.webide_spring_be.domain.project.entity.ProjectMemberRole;
import com.withquery.webide_spring_be.domain.project.repository.ProjectMemberRepository;
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
}
