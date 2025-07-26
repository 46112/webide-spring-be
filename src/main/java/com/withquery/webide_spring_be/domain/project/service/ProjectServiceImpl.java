package com.withquery.webide_spring_be.domain.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withquery.webide_spring_be.domain.file.dto.FileTreeNode;
import com.withquery.webide_spring_be.domain.project.dto.ProjectCreateRequest;
import com.withquery.webide_spring_be.domain.project.dto.ProjectDetailResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectUpdateRequest;
import com.withquery.webide_spring_be.domain.project.entity.Project;
import com.withquery.webide_spring_be.domain.project.entity.ProjectMember;
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

		FileTreeNode fileTree;

		return ProjectDetailResponse.from(project, fileTree);

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
