package com.withquery.webide_spring_be.domain.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withquery.webide_spring_be.domain.project.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {

	List<File> findByProjectIdAndParentFileIsNullOrderByTypeAscNameAsc(Long projectId);

	List<File> findByParentFileIdOrderByTypeAscNameAsc(Long parentId);
	
	Optional<File> findByProjectIdAndPath(Long projectId, String path);

	List<File> findByProjectIdAndType(Long projectId, String type);

	List<File> findByProjectIdAndNameContaining(Long projectId, String keyword);

	Optional<File> findByParentFileIdAndName(Long parentId, String name);

	Long countByParentFileId(Long parentId);

	List<File> findByProjectIdAndTypeOrderByNameAsc(Long projectId, String type);
}
