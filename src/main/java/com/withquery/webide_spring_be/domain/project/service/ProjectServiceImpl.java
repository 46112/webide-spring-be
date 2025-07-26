package com.withquery.webide_spring_be.domain.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withquery.webide_spring_be.domain.project.dto.ProjectCreateRequest;
import com.withquery.webide_spring_be.domain.project.dto.ProjectDetailResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectListResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectUpdateRequest;
import com.withquery.webide_spring_be.domain.project.entity.Project;
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

	@Override
	public ProjectResponse createProject(ProjectCreateRequest request, Long userId) {
		return null;
	}

	@Override
	public List<ProjectListResponse> getMyProjects(Long userId) {
		return List.of();
	}

	@Override
	public ProjectDetailResponse getProjectDetail(Long projectId, Long userId) {
		return null;
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
