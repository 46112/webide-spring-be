package com.withquery.webide_spring_be.domain.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withquery.webide_spring_be.domain.project.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
	
	List<ProjectMember> findByProjectIdAndIsActiveTrue(Long projectId);

	List<ProjectMember> findByUserIdAndIsActiveTrue(Long userId);

	Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

	List<ProjectMember> findByProjectIdAndRole(Long projectId, String role);

	boolean existsByProjectIdAndUserIdAndIsActiveTrue(Long projectId, Long userId);
}