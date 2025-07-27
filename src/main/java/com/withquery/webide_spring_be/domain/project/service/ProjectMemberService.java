package com.withquery.webide_spring_be.domain.project.service;

import java.util.Optional;

import com.withquery.webide_spring_be.domain.project.entity.ProjectMemberRole;

public interface ProjectMemberService {

	void addOwnerMember(Long projectId, Long userId);

	Optional<ProjectMemberRole> getMemberRole(Long projectId, Long userId);

	boolean isMember(Long projectId, Long userId);

	boolean hasPermission(Long projectId, Long userId, ProjectMemberRole... requiredRoles);

}
