package com.withquery.webide_spring_be.domain.project.service;

import java.util.List;

import com.withquery.webide_spring_be.domain.project.dto.ProjectCreateRequest;
import com.withquery.webide_spring_be.domain.project.dto.ProjectDetailResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectUpdateRequest;
import com.withquery.webide_spring_be.domain.project.entity.Project;

public interface ProjectService {

	ProjectResponse createProject(ProjectCreateRequest request, String userEmail);

	List<ProjectResponse> getMyProjects(Long userId);

	ProjectDetailResponse getProjectDetail(Long projectId, Long userId);

	ProjectResponse updateProject(Long projectId, ProjectUpdateRequest request, Long userId);

	void deleteProject(Long projectId, Long userId);

	boolean existsProject(Long projectId);

	Project getProject(Long projectId);

}