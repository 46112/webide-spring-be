package com.withquery.webide_spring_be.domain.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.withquery.webide_spring_be.domain.project.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("""
    SELECT DISTINCT p
    FROM Project p
    LEFT JOIN p.members m
    WHERE p.ownerId = :userId OR m.userId = :userId""")
	Page<Project> findProjectsByUserId(@Param("userId") Long userId, Pageable pageable);

	List<Project> findByOwnerId(Long ownerId);

	List<Project> findByNameContainingAndIsPublicTrue(String keyword);

	@Query("""
    SELECT DISTINCT p
    FROM Project p
    LEFT JOIN p.members m
    WHERE (p.ownerId = :userId OR m.userId = :userId)
    AND p.name LIKE %:keyword%""")
	Page<Project> searchUserProjectsByName(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

	@Query("""
	SELECT COUNT(pm)
	FROM ProjectMember pm
	WHERE pm.project.id = :projectId AND pm.isActive = true""")
	Long countActiveMembers(@Param("projectId") Long projectId);

}
