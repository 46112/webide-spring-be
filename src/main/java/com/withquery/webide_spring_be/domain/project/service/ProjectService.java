package com.withquery.webide_spring_be.domain.project.service;

import java.util.List;

import com.withquery.webide_spring_be.domain.project.dto.ProjectCreateRequest;
import com.withquery.webide_spring_be.domain.project.dto.ProjectDetailResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectListResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectResponse;
import com.withquery.webide_spring_be.domain.project.dto.ProjectUpdateRequest;

public interface ProjectService {

	ProjectResponse createProject(ProjectCreateRequest request, Long userId);

	List<ProjectListResponse> getMyProjects(Long userId);

	ProjectDetailResponse getProjectDetail(Long projectId, Long userId);

	ProjectResponse updateProject(Long projectId, ProjectUpdateRequest request, Long userId);

	void deleteProject(Long projectId, Long userId);

}