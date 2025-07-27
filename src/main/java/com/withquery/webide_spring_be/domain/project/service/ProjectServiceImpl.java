package com.withquery.webide_spring_be.domain.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withquery.webide_spring_be.domain.file.dto.FileTreeNode;
import com.withquery.webide_spring_be.domain.file.service.FileService;
import com.withquery.webide_spring_be.domain.project.dto.ProjectCreateRequest;
import com.withquery.webide_spring_be.domain.project.dto.ProjectDetailResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectUpdateRequest;
import com.withquery.webide_spring_be.domain.project.entity.Project;
import com.withquery.webide_spring_be.domain.project.entity.ProjectMember;
import com.withquery.webide_spring_be.domain.project.entity.ProjectMemberRole;
import com.withquery.webide_spring_be.domain.project.repository.ProjectMemberRepository;
import com.withquery.webide_spring_be.domain.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService{

	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final ProjectMemberService projectMemberService;
	private final FileService fileService;

	@Override
	public ProjectResponse createProject(ProjectCreateRequest request, Long userId) {
		Project project = Project.builder()
			.name(request.getName())
			.description(request.getDescription())
			.ownerId(userId)
			.build();

		Project savedProject = projectRepository.save(project);

		projectMemberService.addOwnerMember(savedProject.getId(), userId);

		return ProjectResponse.from(savedProject);
	}

	@Override
	public List<ProjectResponse> getMyProjects(Long userId) {

		List<ProjectMember> members = projectMemberRepository.findByUserIdAndIsActiveTrue(userId);

		return members.stream()
			.map(member -> ProjectResponse.from(member.getProject(), member.getRole()))
			.collect(Collectors.toList());
	}

	@Override
	public ProjectDetailResponse getProjectDetail(Long projectId, Long userId) {
		Project project = getProject(projectId);

		ProjectMemberRole userRole = projectMemberService.getMemberRole(projectId, userId)
			.orElseThrow(() -> new RuntimeException("프로젝트 접근 권한이 없습니다."));

		FileTreeNode fileTree = fileService.getFileTree(projectId);

		return ProjectDetailResponse.from(
			ProjectResponse.from(project, userRole), fileTree);

	}

	@Override
	public ProjectResponse updateProject(Long projectId, ProjectUpdateRequest request, Long userId) {
		return null;
	}

	@Override
	public void deleteProject(Long projectId, Long userId) {

	}

	@Override
	public boolean existsProject(Long projectId) {
		return false;
	}

	@Override
	public Project getProject(Long projectId) {
		return null;
	}
}
