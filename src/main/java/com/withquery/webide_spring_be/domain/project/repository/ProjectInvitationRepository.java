package com.withquery.webide_spring_be.domain.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.withquery.webide_spring_be.domain.project.entity.InvitationStatus;
import com.withquery.webide_spring_be.domain.project.entity.ProjectInvitation;

public interface ProjectInvitationRepository extends JpaRepository<ProjectInvitation, Long> {

	List<ProjectInvitation> findByInviteeIdAndStatus(Long inviteeId, InvitationStatus status);

	List<ProjectInvitation> findByInviteeIdOrderByCreatedAtDesc(Long inviteeId);

	boolean existsByProjectIdAndInviteeIdAndStatus(Long projectId, Long inviteeId, InvitationStatus status);

	@Query("SELECT pi FROM projectInvitation pi WHERE pi.project.id = :projectId")
	List<ProjectInvitation> findByProjectId(@Param("projectId") Long projectId);

	@Query("SELECT pi FROM projectInvitation pi JOIN FETCH pi.project WHERE pi.id = :invitationId")
	List<ProjectInvitation> findByIdWithProject(@Param("invitationId") Long invitationId);

}
