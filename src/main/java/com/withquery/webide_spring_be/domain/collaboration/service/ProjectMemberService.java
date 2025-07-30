package com.withquery.webide_spring_be.domain.collaboration.service;

import java.util.Optional;

import com.withquery.webide_spring_be.domain.collaboration.entity.ProjectMemberRole;
import com.withquery.webide_spring_be.domain.user.entity.User;

public interface ProjectMemberService {

	User getUserByEmail(String userEmail);

	void addOwnerMember(Long projectId, String userEmail);

	Optional<ProjectMemberRole> getMemberRole(Long projectId, String userEmail);

	boolean isMember(Long projectId, String userEmail);

	boolean hasPermission(Long projectId, String userEmail, ProjectMemberRole... requiredRoles);

}
